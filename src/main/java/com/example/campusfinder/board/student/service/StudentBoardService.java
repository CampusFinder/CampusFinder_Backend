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
import jakarta.servlet.http.HttpServlet;
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
                studentBoard.isNearCampus(),
                studentBoard.getCategoryType()
        );
    }


    // 학생 찾기 글 조회 로직
    @Transactional(readOnly = true)
    public List<StudentBoardDto> getStudentBoardListByCategory(CategoryType categoryType) {
        return studentBoardRepository.findAllByCategoryType(categoryType).stream()
                .map(studentBoard -> new StudentBoardDto(
                        studentBoard.getBoardIdx(),
                        studentBoard.getTitle(),
                        studentBoard.getNickname(),
                        studentBoard.getThumbnailImage(),
                        studentBoard.isNearCampus(),
                        studentBoard.getCategoryType()
                ))
                .toList();
    }

    @Transactional
    public StudentBoardDto updateStudentBoard(HttpServletRequest request, Long boardIdx, StudentBoardRequestDto requestDto, List<MultipartFile> newImages) throws IOException {
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        // 수정할 게시글 조회
        StudentBoard studentBoard = studentBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 게시글 작성자가 아닌 경우 예외 처리
        if (!studentBoard.getNickname().equals(userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다.")).getNickname())) {
            throw new IllegalArgumentException("작성자만 게시글을 수정할 수 있습니다.");
        }

        // 게시글 정보 수정
        studentBoard.toBuilder()
                .title(requestDto.title())
                .isNearCampus(requestDto.isNearCampus())
                .categoryType(requestDto.categoryType())
                .meetingType(requestDto.meetingType())
                .content(requestDto.content())
                .build();

        // 삭제할 이미지 처리 (S3 및 DB에서 삭제)
        if (requestDto.deletedImageUrls() != null) {
            List<StudentBoardImage> imagesToDelete = studentBoard.getImages().stream()
                    .filter(image -> requestDto.deletedImageUrls().contains(image.getImageUrl()))
                    .collect(Collectors.toList());
            for (StudentBoardImage image : imagesToDelete) {
                s3Domain.deleteFile(image.getImageUrl()); // S3에서 이미지 삭제
                studentBoard.getImages().remove(image); // DB에서 이미지 엔티티 삭제
            }
        }

        // 새로운 이미지 추가 (S3 업로드 및 DB 추가)
        String thumbnailImage = studentBoard.getThumbnailImage();
        if (newImages != null && !newImages.isEmpty()) {
            List<String> newImageUrls = newImages.stream()
                    .map(image -> {
                        try {
                            return s3Domain.uploadMultipartFile(image);
                        } catch (IOException e) {
                            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());

            for (String imageUrl : newImageUrls) {
                StudentBoardImage boardImage = StudentBoardImage.builder()
                        .imageUrl(imageUrl)
                        .build();
                studentBoard.addImage(boardImage);
            }

            // 첫 번째 이미지를 썸네일로 설정
            thumbnailImage = newImageUrls.get(0);
        }

        // 썸네일 이미지 설정
        studentBoard.toBuilder().thumbnailImage(thumbnailImage).build();

        studentBoardRepository.save(studentBoard);

        return new StudentBoardDto(
                studentBoard.getBoardIdx(),
                studentBoard.getTitle(),
                studentBoard.getNickname(),
                studentBoard.getThumbnailImage(),
                studentBoard.isNearCampus(),
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
