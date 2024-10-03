package com.example.campusfinder.board.request.controller;

import com.example.campusfinder.board.request.dto.request.RequestBoardRequestDto;
import com.example.campusfinder.board.request.dto.response.RequestBoardDto;
import com.example.campusfinder.board.request.service.RequestBoardService;
import com.example.campusfinder.board.request.service.RequestBoardSortService;
import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.home.entity.CategoryType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.student.controller
 * fileName       : ReqeustBoardController
 * author         : tlswl
 * date           : 2024-10-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl       최초 생성
 */
@RestController
@RequestMapping("/api/request-board")
@RequiredArgsConstructor
@Tag(name = "의뢰 찾기 게시판 API", description = "의뢰 찾기 게시판 관련 API")
public class RequestBoardController {

    private final RequestBoardService requestBoardService;
    private final RequestBoardSortService requestBoardSortService;

    @Operation(
            summary = "의뢰 찾기 글 작성",
            description = "의뢰 찾기 게시판에 새로운 글을 작성합니다.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "의뢰 게시글 작성 요청 데이터",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RequestBoardRequestDto.class),
                            examples = @ExampleObject(
                                    name = "의뢰 게시글 작성 예시",
                                    value = """
                                            {
                                                "categoryType": "DESIGN",
                                                "title": "지금 학교에서 ppt 화면 제작해주실 분 구해요~!",
                                                "money": 10000,
                                                "agreeable": true,
                                                "deadline": "2024-12-31",
                                                "meetingType": "FACE_TO_FACE",
                                                "isUrgent": true,
                                                "content": "급하게 ppt 디자인 제작이 필요합니다.",
                                                "images": []
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "의뢰 찾기 글 작성 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "의뢰 게시글 작성 성공 예시",
                                    value = """
                                            {
                                                "status": 201,
                                                "message": "성공",
                                                "data": {
                                                    "boardIdx": 1,
                                                    "title": "지금 학교에서 ppt 화면 제작해주실 분 구해요~!",
                                                    "nickname": "student123",
                                                    "thumbnailImage": "image1.jpg",
                                                    "isUrgent": true,
                                                    "money": 10000,
                                                    "categoryType": "DESIGN"
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
    public ResponseEntity<BaseResponse<RequestBoardDto>> createRequestBoard(
            HttpServletRequest request,
            @ModelAttribute RequestBoardRequestDto requestDto
    ) throws IOException {
        RequestBoardDto createdBoard = requestBoardService.createRequestBoard(request, requestDto);
        return ResponseEntity.ok(BaseResponse.ofSuccess(201, createdBoard));
    }

    @Operation(
            summary = "의뢰 찾기 게시글 리스트 조회",
            description = "카테고리별로 또는 전체 의뢰 게시글 목록을 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "categoryType",
                            description = "조회할 카테고리 타입. 지정하지 않으면 전체 게시글 조회",
                            example = "DESIGN",
                            required = false
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "카테고리별 게시글 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "카테고리별 게시글 조회 성공 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "성공",
                                                "data": [
                                                    {
                                                        "boardIdx": 1,
                                                        "title": "지금 학교에서 ppt 화면 제작해주실 분 구해요~!",
                                                        "nickname": "student123",
                                                        "thumbnailImage": "image1.jpg",
                                                        "isUrgent": true,
                                                        "money": 10000,
                                                        "categoryType": "DESIGN"
                                                    },
                                                    {
                                                        "boardIdx": 2,
                                                        "title": "웹 개발 프로젝트 팀원 구합니다.",
                                                        "nickname": "student456",
                                                        "thumbnailImage": "image2.jpg",
                                                        "isUrgent": false,
                                                        "money": 20000,
                                                        "categoryType": "DEVELOPMENT"
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
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<RequestBoardDto>>> getRequestBoardsByCategory(
            @RequestParam(required = false) CategoryType categoryType
    ) {
        List<RequestBoardDto> requestBoardList = requestBoardService.getRequestBoardsByCategoryOrAll(categoryType);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, requestBoardList));
    }

    @Operation(
            summary = "의뢰찾기 게시판 시간순 조회",
            description = "카테고리별로 게시글 목록을 최신순 또는 오래된순으로 조회합니다.",
            parameters = {
                    @Parameter(
                            name = "categoryType",
                            description = "조회할 카테고리 타입",
                            example = "DESIGN",
                            required = true
                    ),
                    @Parameter(
                            name = "isLatest",
                            description = "정렬 기준 (true: 최신순, false: 오래된순)",
                            example = "true",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "카테고리별 게시글 정렬 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "카테고리별 게시글 정렬 조회 성공 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "성공",
                                                "data": [
                                                    {
                                                        "boardIdx": 1,
                                                        "title": "지금 학교에서 ppt 화면 제작해주실 분 구해요~!",
                                                        "nickname": "student123",
                                                        "thumbnailImage": "image1.jpg",
                                                        "isUrgent": true,
                                                        "money": 10000,
                                                        "categoryType": "DESIGN"
                                                    },
                                                    {
                                                        "boardIdx": 2,
                                                        "title": "웹 개발 프로젝트 팀원 구합니다.",
                                                        "nickname": "student456",
                                                        "thumbnailImage": "image2.jpg",
                                                        "isUrgent": false,
                                                        "money": 20000,
                                                        "categoryType": "DEVELOPMENT"
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
    @GetMapping("/category/sort")
    public ResponseEntity<BaseResponse<List<RequestBoardDto>>> getRequestBoardsSortedByDate(
            @RequestParam(required = false) CategoryType categoryType, // 카테고리 선택적 파라미터로 변경
            @RequestParam String sortType // 정렬 방식 파라미터 추가
    ) {
        List<RequestBoardDto> sortedRequestBoardList = requestBoardSortService.getRequestBoardsSortedByDate(categoryType, sortType);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, sortedRequestBoardList));
    }

    @Operation(summary = "의뢰 찾기 글 수정", description = "특정 의뢰 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "의뢰 게시글 수정 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PutMapping("/{boardIdx}")
    public ResponseEntity<BaseResponse<RequestBoardDto>> updateRequestBoard(
            @PathVariable Long boardIdx,
            HttpServletRequest request,
            @RequestBody RequestBoardRequestDto requestDto
    ) throws IOException {
        RequestBoardDto updatedBoard = requestBoardService.updateRequestBoard(boardIdx, request, requestDto);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, updatedBoard));
    }

    @Operation(summary = "의뢰 찾기 글 삭제", description = "특정 의뢰 게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "의뢰 게시글 삭제 성공",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<BaseResponse<Void>> deleteRequestBoard(
            @PathVariable Long boardIdx,
            HttpServletRequest request
    ) {
        requestBoardService.deleteRequestBoard(boardIdx, request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, null));
    }


}