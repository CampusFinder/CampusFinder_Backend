package com.example.campusfinder.board.request.repository;

import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.home.entity.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.repository
 * fileName       : RequestBoardRepository
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
@Repository
public interface RequestBoardRepository extends JpaRepository<RequestBoard, Long> {
    List<RequestBoard> findAllByCategoryType(CategoryType categoryType); // 카테고리별 요청 게시판 조회

    // 최신순 정렬
    List<RequestBoard> findAllByCategoryTypeOrderByCreatedAtDesc(CategoryType categoryType);

    // 오래된순 정렬
    List<RequestBoard> findAllByCategoryTypeOrderByCreatedAtAsc(CategoryType categoryType);
}