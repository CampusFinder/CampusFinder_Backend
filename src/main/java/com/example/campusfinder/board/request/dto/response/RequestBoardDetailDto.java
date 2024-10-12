package com.example.campusfinder.board.request.dto.response;

import com.example.campusfinder.board.coinstant.MeetingType;
import com.example.campusfinder.home.entity.CategoryType;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.request.dto.response
 * fileName       : RequestBoardDetailDto
 * author         : tlswl
 * date           : 2024-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-04        tlswl       최초 생성
 */
public record RequestBoardDetailDto(
        Long boardIdx,
        CategoryType categoryType,
        String title,
        String content,
        Integer money,
        MeetingType meetingType,
        LocalDate deadline,
        String nickname,
        String profileImage,
        int viewCount,
        List<String> imageUrls
) {
}
