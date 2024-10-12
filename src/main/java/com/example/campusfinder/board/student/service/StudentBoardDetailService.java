package com.example.campusfinder.board.student.service;

import com.example.campusfinder.board.student.dto.StudentBoardDetailDto;
import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.board.student.entity.StudentBoardImage;



import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.service
 * fileName       : StudentBoardDetailService
 * author         : tlswl
 * date           : 2024-10-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-03        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StudentBoardDetailService {

    private final StudentBoardRepository studentBoardRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 게시글 상세 조회 로직
     * @param boardIdx 게시글 ID
     * @param request HttpServletRequest 객체를 통해 토큰 정보 추출
     * @return 게시글 상세 정보
     */
    public StudentBoardDetailDto getStudentBoardDetail(Long boardIdx, HttpServletRequest request) {
        // JWT 토큰에서 사용자 ID 추출
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        // 게시글 조회 및 유효성 검사
        StudentBoard studentBoard = studentBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 작성자 정보 조회
        UserEntity user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        // 조회수 증가
        studentBoard.incrementViewCount(); // 조회수 증가
        studentBoardRepository.save(studentBoard);

        // 게시글에 등록된 이미지 URL 목록 추출
        List<String> imageUrls = studentBoard.getImages().stream()
                .map(StudentBoardImage::getImageUrl)
                .toList();

        // 게시글 상세 정보 생성 및 반환
        return new StudentBoardDetailDto(
                studentBoard.getBoardIdx(),
                studentBoard.getTitle(),
                studentBoard.getContent(),
                studentBoard.getViewCount(),  // 조회수 반환
                studentBoard.getMeetingType(),
                studentBoard.getIsNearCampus(),
                imageUrls,
                studentBoard.getNickname(),
                user.getProfileImageUrl() // 프로필 이미지 URL 포함
        );
    }
}
