package com.example.campusfinder.board.student.dto;

import com.example.campusfinder.home.entity.CategoryType;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.dto
 * fileName       : StudentSearchDto
 * author         : tlswl
 * date           : 2024-10-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        tlswl       최초 생성
 */
public record StudentSearchDto(
        String keyword,
        List<CategoryType> categories  // 중복 선택 가능한 카테고리 목록
) {
}
