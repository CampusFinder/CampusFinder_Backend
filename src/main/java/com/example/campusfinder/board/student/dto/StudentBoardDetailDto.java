package com.example.campusfinder.board.student.dto;

import com.example.campusfinder.board.coinstant.MeetingType;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.dto
 * fileName       : StudentBoardDetailDto
 * author         : tlswl
 * date           : 2024-10-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-03        tlswl       최초 생성
 */
public record StudentBoardDetailDto(
        Long boardIdx,
        String title,
        String content,
        int viewCount,
        MeetingType meetingType,
        Boolean isNearCampus,
        List<String> imageUrls,

        String nickname,
        String profileImageUrl

) {
}
