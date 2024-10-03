package com.example.campusfinder.board.request.controller;

import com.example.campusfinder.board.request.dto.response.RequestBoardDetailDto;
import com.example.campusfinder.board.request.service.RequestBoardDetailService;
import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.core.security.JwtTokenProvider;
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

@RestController
@RequestMapping("/api/request-board")
@RequiredArgsConstructor
@Tag(name = "의뢰 찾기 게시판 상세 API", description = "의뢰 찾기 게시판의 게시글 상세 조회 API")
public class RequestBoardDetailController {

    private final RequestBoardDetailService requestBoardDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "의뢰 게시글 상세 조회", description = "특정 게시글의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "게시글 상세 조회 성공 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "성공",
                                                "data": {
                                                    "boardIdx": 1,
                                                    "categoryType": "DESIGN",
                                                    "title": "PPT 제작해주실 분 구합니다",
                                                    "content": "급하게 PPT 제작이 필요합니다.",
                                                    "money": 10000,
                                                    "meetingType": "FACE_TO_FACE",
                                                    "deadline": "2024-12-31",
                                                    "nickname": "student123",
                                                    "profileImage": "https://example.com/profile_image.jpg",
                                                    "viewCount": 15,
                                                    "imageUrls": [
                                                        "https://example.com/image1.jpg",
                                                        "https://example.com/image2.jpg"
                                                    ]
                                                }
                                            }
                                            """
                            )
                    )),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "401", description = "권한 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @GetMapping("/detail/{boardIdx}")
    public ResponseEntity<BaseResponse<RequestBoardDetailDto>> getRequestBoardDetail(
            @Parameter(description = "게시글 ID", required = true) @PathVariable Long boardIdx,
            HttpServletRequest request
    ) {
        RequestBoardDetailDto requestBoardDetail = requestBoardDetailService.getRequestBoardDetail(boardIdx, request);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, requestBoardDetail));
    }
}
