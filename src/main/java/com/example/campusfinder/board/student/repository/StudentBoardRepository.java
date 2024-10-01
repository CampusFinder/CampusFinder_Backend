package com.example.campusfinder.board.student.repository;

import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.home.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.request.repository
 * fileName       : StudentBoardRepository
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
public interface StudentBoardRepository extends JpaRepository<StudentBoard, Long> {
    List<StudentBoard> findAllByCategoryType(CategoryType categoryType); // 카테고리별 학생 게시판 조회
}