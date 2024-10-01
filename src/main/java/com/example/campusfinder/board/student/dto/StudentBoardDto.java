package com.example.campusfinder.board.student.dto;

import com.example.campusfinder.home.entity.CategoryType;

/**
 * packageName    : com.example.campusfinder.board.request.dto
 * fileName       : StudentBoardDto
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
public record StudentBoardDto(
        Long boardIdx,
        String title,
        String nickname,
        String thumbnailImage,
        boolean isNearCampus,
        CategoryType categoryType
) { }