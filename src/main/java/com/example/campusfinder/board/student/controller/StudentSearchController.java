package com.example.campusfinder.board.student.controller;

import com.example.campusfinder.board.student.dto.StudentBoardDto;
import com.example.campusfinder.board.student.dto.StudentSearchDto;
import com.example.campusfinder.board.student.service.BoardSearchService;
import com.example.campusfinder.core.base.BaseResponse;
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
            parameters = {
                    @Parameter(name = "keyword", description = "검색 키워드", example = "프로그래밍", required = false),
                    @Parameter(name = "categories", description = "카테고리 리스트", example = "[\"DEV\", \"DESIGN\"]", required = false)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색 결과 조회 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping
    public ResponseEntity<BaseResponse<List<StudentBoardDto>>> searchStudentBoards(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) List<CategoryType> categories // 이 부분이 null로 들어올 수 있음
    ) {
        // categories가 null일 경우 빈 리스트로 초기화
        if (categories == null) {
            categories = List.of();  // 빈 리스트로 설정
        }

        // 검색 DTO 생성
        StudentSearchDto searchDto = new StudentSearchDto(keyword, categories);

        // 검색 서비스 호출
        List<StudentBoardDto> searchResults = boardSearchService.searchStudentBoards(searchDto);

        return ResponseEntity.ok(BaseResponse.ofSuccess(200, searchResults));
    }
}
