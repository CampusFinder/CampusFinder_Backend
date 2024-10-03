package com.example.campusfinder.board.request.service;

import com.example.campusfinder.board.request.dto.response.RequestBoardDetailDto;
import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.board.request.repository.RequestBoardRepository;
import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestBoardDetailService {

    private final RequestBoardRepository requestBoardRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 특정 게시글의 상세 정보 조회
     * @param boardIdx 게시글 인덱스
     * @param request HttpServletRequest 객체를 통해 토큰 정보 추출
     * @return RequestBoardDetailDto 게시글 상세 정보 DTO
     */
    @Transactional
    public RequestBoardDetailDto getRequestBoardDetail(Long boardIdx, HttpServletRequest request) {
        // JWT 토큰에서 사용자 닉네임 추출
        String token = jwtTokenProvider.resolveToken(request);
        String nickname = jwtTokenProvider.getNicknameFromToken(token);

        // 게시글 조회
        RequestBoard requestBoard = requestBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        // 작성자 정보 조회
        Optional<UserEntity> user = userRepository.findByNickname(nickname);

        // 조회수 증가 (setViewCount 대신 incrementViewCount 메서드 활용)
        requestBoard.incrementViewCount();

        // 이미지 URL 리스트 추출
        List<String> imageUrls = requestBoard.getImages().stream()
                .map(image -> image.getImageUrl())
                .toList();

        // 상세 정보 DTO 생성 및 반환
        return new RequestBoardDetailDto(
                requestBoard.getBoardIdx(),
                requestBoard.getCategoryType(),
                requestBoard.getTitle(),
                requestBoard.getContent(),
                requestBoard.getMoney(),
                requestBoard.getMeetingType(),
                requestBoard.getDeadline(),
                requestBoard.getNickname(),
                user.map(UserEntity::getProfileImageUrl).orElse(null), // 작성자 프로필 이미지
                requestBoard.getViewCount(), // 조회수
                imageUrls
        );
    }
}
