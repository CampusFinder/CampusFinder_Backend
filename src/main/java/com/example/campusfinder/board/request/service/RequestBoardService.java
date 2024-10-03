package com.example.campusfinder.board.request.service;

import com.example.campusfinder.board.request.dto.request.RequestBoardRequestDto;
import com.example.campusfinder.board.request.dto.response.RequestBoardDto;
import com.example.campusfinder.board.request.entity.RequestBoard;
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
                .isNegotiable(requestDto.agreeable()) // 합의 가능 여부
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
                requestBoard.isUrgent(),
                requestBoard.getMoney(),
                requestBoard.getCategoryType()
        );
    }

    /**
     * 카테고리별 게시글 조회 로직
     * @param categoryType 카테고리 타입
     * @return List<RequestBoardResponseDto> 게시글 목록 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<RequestBoardDto> getRequestBoardsByCategory(CategoryType categoryType) {
        List<RequestBoard> boards = requestBoardRepository.findAllByCategoryType(categoryType);
        return boards.stream()
                .map(board -> new RequestBoardDto(
                        board.getBoardIdx(),
                        board.getTitle(),
                        board.getNickname(),
                        board.getThumbnailImage(),
                        board.isUrgent(),
                        board.getMoney(),
                        board.getCategoryType()
                )).collect(Collectors.toList());
    }

    /**
     * 카테고리별 게시글 정렬 조회 로직 (최신순, 오래된순)
     * @param categoryType 카테고리 타입
     * @param isLatest 최신순 여부 (true: 최신순, false: 오래된순)
     * @return List<RequestBoardResponseDto> 게시글 목록 응답 DTO 리스트
     */
    @Transactional(readOnly = true)
    public List<RequestBoardDto> getRequestBoardsSortedByDate(CategoryType categoryType, boolean isLatest) {
        List<RequestBoard> boards = isLatest ?
                requestBoardRepository.findAllByCategoryTypeOrderByCreatedAtDesc(categoryType) : // 최신순 정렬
                requestBoardRepository.findAllByCategoryTypeOrderByCreatedAtAsc(categoryType);   // 오래된순 정렬

        return boards.stream()
                .map(board -> new RequestBoardDto(
                        board.getBoardIdx(),
                        board.getTitle(),
                        board.getNickname(),
                        board.getThumbnailImage(),
                        board.isUrgent(),
                        board.getMoney(),
                        board.getCategoryType()
                )).collect(Collectors.toList());
    }

    /**
     * 의뢰 게시글 수정 메서드
     */
    @Transactional
    public RequestBoardDto updateRequestBoard(Long boardIdx, HttpServletRequest request, RequestBoardRequestDto requestBoardDto) throws IOException {
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);
        UserEntity user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        RequestBoard requestBoard = requestBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!requestBoard.getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("해당 게시글을 수정할 권한이 없습니다.");
        }

        // 기존 이미지 유지, 삭제, 추가 관리
        List<String> existingImageUrls = requestBoardDto.imageUrls(); // 기존 이미지 URL 목록
        List<String> newImageUrls = null;

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
        }

        // 최종 이미지 URL 목록을 합침
        List<String> finalImageUrls = existingImageUrls;
        if (newImageUrls != null) {
            finalImageUrls.addAll(newImageUrls);
        }

        // 첫 번째 이미지를 썸네일로 설정
        String thumbnailImage = finalImageUrls.isEmpty() ? null : finalImageUrls.get(0);

        // 수정된 데이터로 게시글 업데이트
        requestBoard = requestBoard.toBuilder()
                .title(requestBoardDto.title())
                .content(requestBoardDto.content())
                .thumbnailImage(thumbnailImage)
                .isUrgent(requestBoardDto.isUrgent())
                .money(requestBoardDto.money())
                .deadline(requestBoardDto.deadline())
                .isNegotiable(requestBoardDto.agreeable())
                .meetingType(requestBoardDto.meetingType())
                .categoryType(requestBoardDto.categoryType())
                .build();

        requestBoardRepository.save(requestBoard);

        return new RequestBoardDto(
                requestBoard.getBoardIdx(),
                requestBoard.getTitle(),
                requestBoard.getNickname(),
                requestBoard.getThumbnailImage(),
                requestBoard.isUrgent(),
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