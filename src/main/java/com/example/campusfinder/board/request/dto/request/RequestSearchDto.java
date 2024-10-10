package com.example.campusfinder.board.request.dto.request;

import com.example.campusfinder.home.entity.CategoryType;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.request.dto.request
 * fileName       : RequestSearchDto
 * author         : tlswl
 * date           : 2024-10-11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-11        tlswl       최초 생성
 */
public record RequestSearchDto(
        String keyword,
        List<CategoryType> categories  // 중복 선택 가능한 카테고리 목록
) {
}
