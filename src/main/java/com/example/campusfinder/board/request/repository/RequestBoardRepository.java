package com.example.campusfinder.board.request.repository;

import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.home.entity.CategoryType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    // 카테고리별 정렬된 게시글 조회
    List<RequestBoard> findAllByCategoryType(CategoryType categoryType, Sort sort);

    // 전체 정렬된 게시글 조회
    List<RequestBoard> findAll(Sort sort);

    // 제목 또는 내용에 특정 키워드가 포함된 모든 게시글을 조회하는 메서드
    List<RequestBoard> findAllByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // 키워드와 특정 카테고리들 내에서 게시글을 검색하는 커스텀 쿼리 메서드
    @Query("SELECT b FROM RequestBoard b WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND (:categories IS NULL OR b.categoryType IN :categories)")
    List<RequestBoard> searchByKeywordAndCategoryTypes(
            @Param("keyword") String keyword,
            @Param("categories") List<CategoryType> categories
    );
}