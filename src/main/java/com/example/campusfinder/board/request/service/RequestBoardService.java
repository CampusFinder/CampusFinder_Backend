package com.example.campusfinder.board.request.service;

import com.example.campusfinder.board.request.dto.request.RequestBoardRequestDto;
import com.example.campusfinder.board.request.dto.response.RequestBoardDto;
import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.board.request.entity.RequestBoardImage;
import com.example.campusfinder.board.request.repository.RequestBoardRepository;
import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.core.util.S3Domain;
import com.example.campusfinder.home.entity.CategoryType;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.campusfinder.board.student.service
 * fileName       : RequestBoardService
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
public class RequestBoardService {

    private final RequestBoardRepository requestBoardRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Domain s3Domain;

    /**
     * 의뢰 게시글 작성 로직
     * @param request HTTP 요청 객체
     * @param requestDto 게시글 작성 DTO
     * @return RequestBoardResponseDto 게시글 응답 DTO
     * @throws IOException 파일 업로드 실패 시 예외 발생
     */
    @Transactional
    public RequestBoardDto createRequestBoard(HttpServletRequest request, RequestBoardRequestDto requestDto) throws IOException {
        // JWT 토큰에서 userIdx 추출
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        // UserEntity에서 닉네임 가져오기
        UserEntity user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));
        String nickname = user.getNickname();

        // S3에 이미지 업로드 및 썸네일 이미지 설정
        String thumbnailImage = null;
        if (requestDto.images() != null && !requestDto.images().isEmpty()) {
            List<String> imageUrls = requestDto.images().stream()
                    .map(image -> {
                        try {
                            return s3Domain.uploadMultipartFile(image);
                        } catch (IOException e) {
                            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());

            // 첫 번째 이미지를 썸네일로 설정
            thumbnailImage = imageUrls.get(0);
        }

        // RequestBoard 엔티티 생성
        RequestBoard requestBoard = RequestBoard.builder()
                .title(requestDto.title())
                .content(requestDto.content())
                .nickname(nickname)
                .thumbnailImage(thumbnailImage)
                .isUrgent(requestDto.isUrgent())
                .isNegotiable(requestDto.isNegotiable()) // 합의 가능 여부
                .money(requestDto.money())
                .deadline(requestDto.deadline()) // 마감기한
                .meetingType(requestDto.meetingType())
                .categoryType(requestDto.categoryType())
                .build();

        // 저장
        requestBoardRepository.save(requestBoard);

        // 반환 DTO 생성
        return new RequestBoardDto(
                requestBoard.getBoardIdx(),
                requestBoard.getTitle(),
                requestBoard.getNickname(),
                requestBoard.getThumbnailImage(),
                requestBoard.getIsUrgent(),
                requestBoard.getMoney(),
                requestBoard.getCategoryType()
        );
    }

    /**
     * 카테고리별 또는 전체 RequestBoard 게시글 조회
     * @param categoryType 카테고리 타입 (null이면 전체 조회)
     * @return RequestBoardDto 목록
     */
    @Transactional(readOnly = true)
    public List<RequestBoardDto> getRequestBoardsByCategoryOrAll(CategoryType categoryType) {
        List<RequestBoard> boards;

        if (categoryType != null) {
            // 카테고리별 게시글 조회
            boards = requestBoardRepository.findAllByCategoryType(categoryType);
        } else {
            // 전체 게시글 조회
            boards = requestBoardRepository.findAll();
        }

        return boards.stream()
                .map(board -> new RequestBoardDto(
                        board.getBoardIdx(),
                        board.getTitle(),
                        board.getNickname(),
                        board.getThumbnailImage(),
                        board.getIsUrgent(),
                        board.getMoney(),
                        board.getCategoryType()
                )).collect(Collectors.toList());
    }

    /**
     * 의뢰 게시글 수정 메서드
     */
    @Transactional
    public RequestBoardDto updateRequestBoard(Long boardIdx, HttpServletRequest request, RequestBoardRequestDto requestBoardDto) throws IOException {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);
        UserEntity user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 게시글 조회 및 수정 권한 확인
        RequestBoard requestBoard = requestBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!requestBoard.getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");
        }

        // 수정할 데이터 적용: Null 체크 후 기존 데이터 유지
        requestBoard = requestBoard.toBuilder()
                .title(requestBoardDto.title() != null ? requestBoardDto.title() : requestBoard.getTitle())
                .content(requestBoardDto.content() != null ? requestBoardDto.content() : requestBoard.getContent())
                .isUrgent(requestBoardDto.isUrgent() != null ? requestBoardDto.isUrgent() : requestBoard.getIsUrgent())
                .money(requestBoardDto.money() != 0 ? requestBoardDto.money() : requestBoard.getMoney())
                .deadline(requestBoardDto.deadline() != null ? requestBoardDto.deadline() : requestBoard.getDeadline())
                .isNegotiable(requestBoardDto.isNegotiable() != null ? requestBoardDto.isNegotiable() : requestBoard.getIsNegotiable())
                .meetingType(requestBoardDto.meetingType() != null ? requestBoardDto.meetingType() : requestBoard.getMeetingType())
                .categoryType(requestBoardDto.categoryType() != null ? requestBoardDto.categoryType() : requestBoard.getCategoryType())
                .build();

        // 기존 이미지 유지 및 삭제 처리
        List<String> existingImageUrls = requestBoardDto.imageUrls() != null ? new ArrayList<>(requestBoardDto.imageUrls()) : new ArrayList<>();
        if (requestBoardDto.deletedImageUrls() != null && !requestBoardDto.deletedImageUrls().isEmpty()) {
            List<RequestBoardImage> imagesToDelete = requestBoard.getImages().stream()
                    .filter(image -> requestBoardDto.deletedImageUrls().contains(image.getImageUrl()))
                    .collect(Collectors.toList());

            for (RequestBoardImage image : imagesToDelete) {
                s3Domain.deleteFile(image.getImageUrl()); // S3에서 이미지 삭제
                requestBoard.getImages().remove(image); // DB에서 이미지 엔티티 삭제
                existingImageUrls.remove(image.getImageUrl()); // 기존 이미지 목록에서도 삭제
            }
        }

        // 새로운 이미지 추가 처리
        List<String> newImageUrls = new ArrayList<>();
        if (requestBoardDto.images() != null && !requestBoardDto.images().isEmpty()) {
            newImageUrls = requestBoardDto.images().stream()
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
        String thumbnailImage = requestBoard.getThumbnailImage();
        if (thumbnailImage == null || (requestBoardDto.deletedImageUrls() != null && requestBoardDto.deletedImageUrls().contains(thumbnailImage))) {
            thumbnailImage = !existingImageUrls.isEmpty() ? existingImageUrls.get(0) : null;
        }

        // 수정된 데이터로 게시글 업데이트 (썸네일 이미지 포함)
        requestBoard = requestBoard.toBuilder()
                .thumbnailImage(thumbnailImage)
                .build();

        // 수정된 게시글 저장
        requestBoardRepository.save(requestBoard);

        // 응답 DTO 생성 및 반환
        return new RequestBoardDto(
                requestBoard.getBoardIdx(),
                requestBoard.getTitle(),
                requestBoard.getNickname(),
                requestBoard.getThumbnailImage(),
                requestBoard.getIsUrgent(),
                requestBoard.getMoney(),
                requestBoard.getCategoryType()
        );
    }


    @Transactional
    public void deleteRequestBoard(Long boardIdx, HttpServletRequest request) {
        // 토큰에서 userIdx를 추출하고 사용자 정보를 가져옴
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        RequestBoard requestBoard = requestBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!requestBoard.getNickname().equals(userRepository.findById(userIdx)
                .map(UserEntity::getNickname)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다.")))) {
            throw new IllegalArgumentException("해당 게시글을 삭제할 권한이 없습니다.");
        }

        requestBoardRepository.delete(requestBoard);
    }
}