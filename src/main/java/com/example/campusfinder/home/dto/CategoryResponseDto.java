package com.example.campusfinder.home.dto;

import com.example.campusfinder.home.entity.CategoryType;

/**
 * packageName    : com.example.campusfinder.home.dto
 * fileName       : CategoryResponseDto
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
public record CategoryResponseDto(
        CategoryType categoryType,
        String categoryName
) {
}
