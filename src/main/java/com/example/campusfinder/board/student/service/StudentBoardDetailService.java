package com.example.campusfinder.board.student.service;

import com.example.campusfinder.board.student.dto.StudentBoardDetailDto;
import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.core.util.S3Domain;
import com.example.campusfinder.user.repository.UserRepository;
import io.jsonwebtoken.Jwt;
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
     * @param userIdx 현재 로그인된 사용자의 ID
     * @return 게시글 상세 정보
     */
    @Transactional
    public StudentBoardDetailDto getStudentBoardDetail(Long boardIdx, Long userIdx) {
        // 게시글 조회
        StudentBoard studentBoard = studentBoardRepository.findById(boardIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        // 조회수 증가
        studentBoardRepository.incrementViewCount(boardIdx);

        // 유효하지 않은 사용자에 대한 예외 처리
        UserEntity user = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("권한이 없습니다."));

        // 작성자가 아닌 경우 예외 처리
        if (!studentBoard.getNickname().equals(user.getNickname())) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 사용자 프로필 이미지 URL 가져오기
        String profileImageUrl = user.getProfileImageUrl(); // 프로필 이미지 URL 추출

        // 게시글에 등록된 이미지 URL 목록 추출
        List<String> imageUrls = studentBoard.getImages().stream()
                .map(StudentBoardImage::getImageUrl)
                .toList();

        // 게시글 상세 정보를 반환
        return new StudentBoardDetailDto(
                studentBoard.getBoardIdx(),
                studentBoard.getTitle(),
                studentBoard.getContent(),
                studentBoard.getViewCount(),  // 조회수 반환
                studentBoard.getMeetingType(),
                studentBoard.isNearCampus(),
                imageUrls,
                profileImageUrl,  // 프로필 이미지 URL 포함
                studentBoard.getNickname()
        );
    }
}

