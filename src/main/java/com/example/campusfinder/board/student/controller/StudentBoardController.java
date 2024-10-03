package com.example.campusfinder.board.student.controller;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentBoardRequestDto;
import com.example.campusfinder.board.student.service.StudentBoardService;
import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.core.security.JwtTokenProvider;
import com.example.campusfinder.home.entity.CategoryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.controller
 * fileName       : StudentBoardController
 * author         : tlswl
 * date           : 2024-10-01
 * description    : 학생 찾기 게시판 관련 API Controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl              최초 생성
 */
@RestController
@RequestMapping("/api/student-board")
@RequiredArgsConstructor
@Tag(name = "학생찾기 게시판 API", description = "학생 찾기 게시판 관련 API")
public class StudentBoardController {

    private final StudentBoardService studentBoardService;

    @Operation(
            summary = "학생 찾기 글 작성",
            description = "학생 찾기 게시판에 새로운 글을 작성합니다. 작성자의 닉네임은 로그인된 사용자의 정보를 통해 자동으로 가져옵니다.",
            requestBody = @RequestBody(
                    description = "학생 찾기 게시판 글 작성 요청",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudentBoardRequestDto.class),
                            examples = @ExampleObject(
                                    name = "학생 찾기 글 작성 예시",
                                    value = """
                                            {
                                                "categoryType": "PROGRAMMING",
                                                "title": "프로그래밍 수업 과제 도움",
                                                "isNearCampus": true,
                                                "meetingType": "OFFLINE",
                                                "content": "프로그래밍 수업 과제 도와주실 분 구합니다.",
                                                "imageUrls": ["image1.jpg", "image2.jpg"]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "학생 찾기 글 작성 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = """
                                            {
                                                "status": 201,
                                                "message": "성공",
                                                "data": {
                                                    "boardIdx": 1,
                                                    "title": "프로그래밍 수업 과제 도움",
                                                    "nickname": "student123",
                                                    "thumbnailImage": "image1.jpg",
                                                    "isNearCampus": true,
                                                    "categoryType": "PROGRAMMING"
                                                }
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<BaseResponse<StudentBoardDto>> createStudentBoard(
            HttpServletRequest request,
            @RequestPart StudentBoardRequestDto requestDto,
            @RequestPart(required = false) List<MultipartFile> images
    ) throws IOException
    {
        StudentBoardDto createdBoard = studentBoardService.createStudentBoard(request, requestDto, images);
        return ResponseEntity.ok(BaseResponse.ofSuccess(201, createdBoard));
    }

    @Operation(
            summary = "학생 찾기 게시글 조회",
            description = "특정 카테고리의 학생 찾기 게시글 목록을 조회합니다. 각 게시글의 썸네일 이미지를 포함하여 조회됩니다.",
            parameters = @Parameter(
                    name = "categoryType",
                    description = "조회할 카테고리 타입",
                    example = "PROGRAMMING",
                    required = true
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 목록 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "성공",
                                                "data": [
                                                    {
                                                        "boardIdx": 1,
                                                        "title": "프로그래밍 수업 과제 도움",
                                                        "nickname": "student123",
                                                        "thumbnailImage": "image1.jpg",
                                                        "isNearCampus": true,
                                                        "categoryType": "PROGRAMMING"
                                                    },
                                                    {
                                                        "boardIdx": 2,
                                                        "title": "웹 개발 프로젝트 팀원 구합니다.",
                                                        "nickname": "student456",
                                                        "thumbnailImage": "image2.jpg",
                                                        "isNearCampus": false,
                                                        "categoryType": "PROGRAMMING"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<StudentBoardDto>>> getStudentBoardListByCategory(
            @RequestParam CategoryType categoryType
    ) {
        List<StudentBoardDto> studentBoardList = studentBoardService.getStudentBoardListByCategory(categoryType);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, studentBoardList));
    }

    @Operation(summary = "학생 찾기 글 수정", description = "학생 찾기 게시판에 작성된 글을 수정합니다. 작성자 본인만 수정 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 찾기 글 수정 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/{boardIdx}")
    public ResponseEntity<BaseResponse<StudentBoardDto>> updateStudentBoard(
            HttpServletRequest request,
            @PathVariable Long boardIdx,
            @RequestPart StudentBoardRequestDto requestDto,
            @RequestPart(required = false) List<MultipartFile> images) throws IOException {
        StudentBoardDto updatedBoard = studentBoardService.updateStudentBoard(request, boardIdx, requestDto, images);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, updatedBoard));
    }

    @Operation(summary = "학생 찾기 글 삭제", description = "학생 찾기 게시판에 작성된 글을 삭제합니다. 작성자 본인만 삭제 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "학생 찾기 글 삭제 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<BaseResponse<Void>> deleteStudentBoard(
            HttpServletRequest request,
            @PathVariable Long boardIdx) {
        studentBoardService.deleteStudentBoard(request, boardIdx);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, null));
    }
}
