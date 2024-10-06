package com.example.campusfinder.board.request.dto.request;

import com.example.campusfinder.board.student.entity.MeetingType;
import com.example.campusfinder.home.entity.CategoryType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.request.dto.request
 * fileName       : RequestBoardCreateRequestDto
 * author         : tlswl
 * date           : 2024-10-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-03        tlswl       최초 생성
 */
public record RequestBoardRequestDto(
        CategoryType categoryType,
        String title,
        Integer money,
        Boolean isNegotiable,
        LocalDate deadline,
        MeetingType meetingType,
        Boolean isUrgent,
        String content,
        List<MultipartFile> uploadImages, // 이미지 리스트 (최대 6장)
        List<String> deletedImageUrls, // 삭제할 이미지 URL 리스트
        List<String> imageUrls // 기존 이미지 URL 목록

) {
}
