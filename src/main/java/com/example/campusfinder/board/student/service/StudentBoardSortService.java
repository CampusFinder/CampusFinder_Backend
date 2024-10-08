package com.example.campusfinder.board.student.service;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import com.example.campusfinder.home.entity.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.service
 * fileName       : StudentBoardSortService
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
public class StudentBoardSortService {

    private final StudentBoardRepository studentBoardRepository;

    /**
     * 카테고리와 정렬 방식에 따라 게시글 목록 조회
     * @param categoryType 카테고리 타입
     * @param sortType 정렬 방식 (true: 최신순, false: 오래된순)
     * @return 정렬된 게시글 목록
     */
    @Transactional(readOnly = true)
    public List<StudentBoardDto> getStudentBoardListByCategoryAndSort(CategoryType categoryType, boolean sortType) {
        // 정렬 방식 설정 (true: 최신순, false: 오래된순)
        Sort sort = sortType ? Sort.by("createdAt").descending() : Sort.by("createdAt").ascending();

        // 카테고리와 정렬 방식에 따라 게시글 조회
        return studentBoardRepository.findAllByCategoryType(categoryType, sort).stream()
                .map(studentBoard -> new StudentBoardDto(
                        studentBoard.getBoardIdx(),
                        studentBoard.getTitle(),
                        studentBoard.getNickname(),
                        studentBoard.getThumbnailImage(),
                        studentBoard.getIsNearCampus(),
                        studentBoard.getCategoryType()
                ))
                .toList();
    }
}
