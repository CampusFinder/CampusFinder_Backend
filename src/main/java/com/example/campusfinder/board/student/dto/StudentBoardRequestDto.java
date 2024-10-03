package com.example.campusfinder.board.student.dto;

import com.example.campusfinder.board.student.entity.MeetingType;
import com.example.campusfinder.home.entity.CategoryType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.dto
 * fileName       : StudentBoardRequestDto
 * author         : tlswl
 * date           : 2024-10-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl       최초 생성
 */
public record StudentBoardRequestDto(
        CategoryType categoryType,
        String title,
        Boolean isNearCampus,
        MeetingType meetingType,
        String content,
        List<String> imageUrls,
        List<MultipartFile> uploadImages, // 새롭게 추가할 이미지 리스트
        List<String> deletedImageUrls // 삭제할 이미지 URL 목록 (추가)

) {
}
