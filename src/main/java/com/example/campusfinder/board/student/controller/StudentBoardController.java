package com.example.campusfinder.board.student.controller;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentBoardRequestDto;
import com.example.campusfinder.board.student.service.StudentBoardService;
import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.home.entity.CategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.controller
 * fileName       : StudentBoardController
 * author         : tlswl
 * date           : 2024-10-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl       최초 생성
 */
@RestController
@RequestMapping("/api/student-board")
@RequiredArgsConstructor
public class StudentBoardController {

    private final StudentBoardService studentBoardService;

    // 학생 찾기 글 작성 API
    @PostMapping
    public ResponseEntity<BaseResponse<StudentBoardDto>> createStudentBoard(
            @RequestBody StudentBoardRequestDto requestDto,
            @RequestParam String nickname // 작성자 닉네임은 요청 매개변수로 받음
    ) {
        StudentBoardDto createdBoard = studentBoardService.createStudentBoard(requestDto, nickname);
        return ResponseEntity.ok(BaseResponse.ofSuccess(201, createdBoard));
    }

    // 특정 카테고리의 학생 찾기 게시글 목록 조회 API
    @GetMapping
    public ResponseEntity<BaseResponse<List<StudentBoardDto>>> getStudentBoardListByCategory(
            @RequestParam CategoryType categoryType
    ) {
        List<StudentBoardDto> studentBoardList = studentBoardService.getStudentBoardListByCategory(categoryType);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, studentBoardList));
    }
}
