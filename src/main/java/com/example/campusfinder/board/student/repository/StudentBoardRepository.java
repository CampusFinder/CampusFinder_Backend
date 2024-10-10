package com.example.campusfinder.board.student.repository;

import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.home.entity.CategoryType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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
    List<StudentBoard> findAllByCategoryType(CategoryType categoryType, Sort sort);

    @Modifying
    @Query("UPDATE StudentBoard sb SET sb.viewCount = sb.viewCount + 1 WHERE sb.boardIdx = :boardIdx")
    void incrementViewCount(@Param("boardIdx") Long boardIdx);

    // 제목 또는 내용에 특정 키워드가 포함된 모든 게시글을 조회하는 메서드
    List<StudentBoard> findAllByTitleContainingOrContentContaining(String titleKeyword, String contentKeyword);

    // 키워드와 특정 카테고리들 내에서 게시글을 검색하는 커스텀 쿼리 메서드
    @Query("SELECT b FROM StudentBoard b WHERE (b.title LIKE %:keyword% OR b.content LIKE %:keyword%) AND b.categoryType IN :categories")
    List<StudentBoard> searchByKeywordAndCategoryTypes(
            @Param("keyword") String keyword,
            @Param("categories") List<CategoryType> categories
    );


}