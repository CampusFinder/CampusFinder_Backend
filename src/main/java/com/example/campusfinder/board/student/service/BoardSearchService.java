package com.example.campusfinder.board.student.service;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentSearchDto;
import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : com.example.campusfinder.board.student.service
 * fileName       : BoardSearchService
 * author         : tlswl
 * date           : 2024-10-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardSearchService {

    private final StudentBoardRepository studentBoardRepository;

    @Transactional(readOnly = true)
    public List<StudentBoardDto> searchStudentBoards(StudentSearchDto searchDto) {
        List<StudentBoard> boards;

        // 카테고리 리스트가 비어있을 경우 전체 카테고리에서 검색
        if (searchDto.categories() == null || searchDto.categories().isEmpty()) {
            // 제목 또는 내용에 키워드가 포함된 게시글 전체 조회
            boards = studentBoardRepository.findAllByTitleContainingOrContentContaining(
                    searchDto.keyword(), searchDto.keyword()
            );
        } else {
            // 키워드가 포함된 게시글 중, 특정 카테고리들 내에서 조회
            boards = studentBoardRepository.searchByKeywordAndCategoryTypes(
                    searchDto.keyword(), searchDto.categories()
            );
        }

        // 결과 DTO로 변환
        return boards.stream()
                .map(board -> new StudentBoardDto(
                        board.getBoardIdx(),
                        board.getTitle(),
                        board.getNickname(),
                        board.getThumbnailImage(),
                        board.getIsNearCampus(),
                        board.getCategoryType()
                ))
                .collect(Collectors.toList());
    }
}
