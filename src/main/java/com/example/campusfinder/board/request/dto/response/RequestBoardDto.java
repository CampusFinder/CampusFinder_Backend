package com.example.campusfinder.board.request.dto.response;

import com.example.campusfinder.home.entity.CategoryType;

/**
 * packageName    : com.example.campusfinder.board.student.dto.request
 * fileName       : RequestBoardDto
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
public record RequestBoardDto(
        Long boardIdx,
        String title,
        String nickname,
        String thumbnailImage,
        Boolean isUrgent,
        int money,
        CategoryType categoryType
) { }