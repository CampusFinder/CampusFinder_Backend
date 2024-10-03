package com.example.campusfinder.board.student.service;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentBoardRequestDto;
import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.board.student.entity.StudentBoardImage;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.core.util.S3Domain;
import com.example.campusfinder.home.entity.CategoryType;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentBoardService {

    private final StudentBoardRepository studentBoardRepository;
    private final UserRepository userRepository;
    private final S3Domain s3Domain;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public StudentBoardDto createStudentBoard(HttpServletRequest request, StudentBoardRequestDto studentBoardRequestDto, List<MultipartFile> images) throws IOException {
        // HttpServletRequest에서 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);

        // 토큰에서 userIdx 추출
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);


        // UserEntity에서 닉네임을 가져옴
        UserEntity user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        String nickname = user.getNickname(); // 사용자 엔티티에서 닉네임 추출

        // S3에 이미지 업로드 및 StudentBoardImage 엔티티 생성
        List<StudentBoardImage> imageEntities = new ArrayList<>();
        String thumbnailImage = null;

        if (images != null && !images.isEmpty()) {
            List<String> imageUrls = images.stream()
                    .map(image -> {
                        try {
                            return s3Domain.uploadMultipartFile(image);
                        } catch (IOException e) {
                            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());

            // StudentBoardImage 엔티티 생성 및 추가
            for (String imageUrl : imageUrls) {
                StudentBoardImage boardImage = StudentBoardImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                imageEntities.add(boardImage);
            }

            // 첫 번째 이미지를 썸네일로 설정
            thumbnailImage = imageUrls.get(0);
        }

        // StudentBoard 엔티티 생성
        StudentBoard studentBoard = StudentBoard.builder()
                .title(studentBoardRequestDto.title())
                .nickname(nickname) // 조회한 사용자의 닉네임을 사용
                .thumbnailImage(thumbnailImage)
                .isNearCampus(studentBoardRequestDto.isNearCampus())
                .categoryType(studentBoardRequestDto.categoryType())
                .meetingType(studentBoardRequestDto.meetingType())
                .content(studentBoardRequestDto.content())
                .build();

        // 이미지 엔티티를 StudentBoard에 추가
        for (StudentBoardImage image : imageEntities) {
            studentBoard.addImage(image);
        }

        studentBoardRepository.save(studentBoard);

        return new StudentBoardDto(
                studentBoard.getBoardIdx(),
                studentBoard.getTitle(),
                studentBoard.getNickname(),
                studentBoard.getThumbnailImage(),
                studentBoard.getIsNearCampus(),
                studentBoard.getCategoryType()
        );
    }


    @Transactional(readOnly = true)
    public List<StudentBoardDto> getStudentBoardListByCategoryOrAll(CategoryType categoryType) {
        List<StudentBoard> boards;
        if (categoryType != null) {
            // 카테고리별 조회
            boards = studentBoardRepository.findAllByCategoryType(categoryType);
        } else {
            // 전체 게시글 조회
            boards = studentBoardRepository.findAll();
        }
        return boards.stream()
                .map(studentBoard -> new StudentBoardDto(
                        studentBoard.getBoardIdx(),
                        studentBoard.getTitle(),
                        studentBoard.getNickname(),
                        studentBoard.getThumbnailImage(),
                        studentBoard.getIsNearCampus(),
                        studentBoard.getCategoryType()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentBoardDto updateStudentBoard(HttpServletRequest request, Long boardIdx, StudentBoardRequestDto requestDto) throws IOException {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        // 게시글 조회 및 수정 권한 체크
        StudentBoard studentBoard = studentBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자와 요청 사용자의 일치 여부 확인
        if (!studentBoard.getNickname().equals(userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다.")).getNickname())) {
            throw new IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 수정할 데이터 적용 (Boolean 타입을 사용하여 null 체크 및 값 대입)
        Boolean isNearCampus = requestDto.isNearCampus(); // null 가능 Boolean 객체로 설정

        studentBoard = studentBoard.toBuilder()
                .title(requestDto.title() != null ? requestDto.title() : studentBoard.getTitle())
                .isNearCampus(isNearCampus != null ? isNearCampus : studentBoard.getIsNearCampus()) // Boolean 값 확인 후 적용
                .categoryType(requestDto.categoryType() != null ? requestDto.categoryType() : studentBoard.getCategoryType())
                .meetingType(requestDto.meetingType() != null ? requestDto.meetingType() : studentBoard.getMeetingType())
                .content(requestDto.content() != null ? requestDto.content() : studentBoard.getContent())
                .build();

        // 기존 이미지 관리 로직
        List<String> existingImageUrls = requestDto.imageUrls() != null ? new ArrayList<>(requestDto.imageUrls()) : new ArrayList<>();

        // 삭제할 이미지 URL 리스트가 전달되었을 경우 삭제 처리
        if (requestDto.deletedImageUrls() != null && !requestDto.deletedImageUrls().isEmpty()) {
            List<StudentBoardImage> imagesToDelete = studentBoard.getImages().stream()
                    .filter(image -> requestDto.deletedImageUrls().contains(image.getImageUrl()))
                    .collect(Collectors.toList());
            for (StudentBoardImage image : imagesToDelete) {
                s3Domain.deleteFile(image.getImageUrl()); // S3에서 이미지 삭제
                studentBoard.getImages().remove(image); // DB에서 이미지 엔티티 삭제
                existingImageUrls.remove(image.getImageUrl()); // 기존 이미지 목록에서도 삭제
            }
        }

        // 새로운 이미지 추가 처리 (S3 업로드 및 DB 추가)
        List<String> newImageUrls = new ArrayList<>();
        if (requestDto.images() != null && !requestDto.images().isEmpty()) {
            newImageUrls = requestDto.images().stream()
                    .map(image -> {
                        try {
                            return s3Domain.uploadMultipartFile(image);
                        } catch (IOException e) {
                            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());
            existingImageUrls.addAll(newImageUrls);
        }

        // 썸네일 이미지 설정: 기존 썸네일 이미지가 삭제되었거나 없을 경우 새 이미지로 설정
        String thumbnailImage = studentBoard.getThumbnailImage();
        if (thumbnailImage == null || (requestDto.deletedImageUrls() != null && requestDto.deletedImageUrls().contains(thumbnailImage))) {
            thumbnailImage = !existingImageUrls.isEmpty() ? existingImageUrls.get(0) : null;
        }

        // 수정된 데이터로 게시글 업데이트 (썸네일 이미지 포함)
        studentBoard = studentBoard.toBuilder()
                .thumbnailImage(thumbnailImage)
                .build();

        // 업데이트된 게시글 저장
        studentBoardRepository.save(studentBoard);

        // 응답 DTO 생성
        return new StudentBoardDto(
                studentBoard.getBoardIdx(),
                studentBoard.getTitle(),
                studentBoard.getNickname(),
                studentBoard.getThumbnailImage(),
                studentBoard.getIsNearCampus(),
                studentBoard.getCategoryType()
        );
    }


    @Transactional
    public void deleteStudentBoard(HttpServletRequest request, Long boardIdx) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        StudentBoard studentBoard = studentBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        if (!studentBoard.getNickname().equals(userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다.")).getNickname())) {
            throw new IllegalArgumentException("작성자만 게시글을 삭제할 수 있습니다.");
        }

        studentBoardRepository.delete(studentBoard);
    }
}
