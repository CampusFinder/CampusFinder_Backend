package com.example.campusfinder.board.student.controller;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentSearchDto;
import com.example.campusfinder.board.student.service.BoardSearchService;
import com.example.campusfinder.core.base.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.controller
 * fileName       : StudentSearchController
 * author         : tlswl
 * date           : 2024-10-08
 * description    : 학생 찾기 게시판 검색 기능을 위한 Controller 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-08        tlswl              최초 생성
 */
@RestController
@RequestMapping("/api/student-board/search")
@RequiredArgsConstructor
@Tag(name = "게시판 검색 API", description = "학생 찾기 게시판의 검색 기능 관련 API")
public class StudentSearchController {

    private final BoardSearchService boardSearchService;

    @Operation(
            summary = "학생 찾기 게시글 검색",
            description = "키워드와 카테고리를 사용하여 학생 찾기 게시글을 검색합니다.",
            requestBody = @RequestBody(
                    description = "검색 요청 DTO",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StudentSearchDto.class),
                            examples = @ExampleObject(
                                    name = "학생 찾기 검색 예시",
                                    value = """
                                            {
                                                "keyword": "프로그래밍",
                                                "categories": ["DEV", "DESIGN"]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "검색 결과 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "검색 결과 예시",
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
                                                        "categoryType": "DEV"
                                                    },
                                                    {
                                                        "boardIdx": 2,
                                                        "title": "프로그래밍 개발 프로젝트 팀원 구합니다.",
                                                        "nickname": "student456",
                                                        "thumbnailImage": "image2.jpg",
                                                        "isNearCampus": false,
                                                        "categoryType": "DESIGN"
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
    @PostMapping
    public ResponseEntity<BaseResponse<List<StudentBoardDto>>> searchStudentBoards(
            @RequestBody StudentSearchDto searchDto) {
        // BoardSearchService를 이용하여 검색 수행
        List<StudentBoardDto> searchResults = boardSearchService.searchStudentBoards(searchDto);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, searchResults));
    }
}
