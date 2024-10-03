package com.example.campusfinder.board.student.controller;

import com.example.campusfinder.board.student.dto.StudentBoardDetailDto;
import com.example.campusfinder.board.student.service.StudentBoardDetailService;
import com.example.campusfinder.board.student.service.StudentBoardService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.campusfinder.board.student.controller
 * fileName       : StudentBoardDetailController
 * author         : tlswl
 * date           : 2024-10-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-03        tlswl       최초 생성
 */
@RestController
@RequestMapping("/api/student-board")
@RequiredArgsConstructor
@Tag(name = "학생찾기 상세 API", description = "학생 찾기 게시판 관련 API")
public class StudentBoardDetailController {

    private final StudentBoardDetailService studentBoardDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "게시글 상세 조회",
            description = "특정 게시글의 상세 정보를 조회합니다. 제목, 조회수, 진행방식, 학교근처 거주 여부, 의뢰내용, 이미지 리스트, 작성자 닉네임 정보를 반환합니다.",
            parameters = {
                    @Parameter(
                            name = "boardIdx",
                            description = "조회할 게시글 ID",
                            example = "1",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "게시글 상세 정보 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "성공 응답 예시",
                                    value = """
                                            {
                                                "status": 200,
                                                "message": "성공",
                                                "data": {
                                                    "boardIdx": 1,
                                                    "title": "프로그래밍 수업 과제 도움",
                                                    "content": "프로그래밍 수업 과제 도와주실 분 구합니다.",
                                                    "viewCount": 15,
                                                    "meetingType": "FACE-TO-FACE",
                                                    "isNearCampus": true,
                                                    "imageUrls": ["image1.jpg", "image2.jpg"],
                                                    "nickname": "student123"
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
    @GetMapping("/detail/{boardIdx}")
    public ResponseEntity<BaseResponse<StudentBoardDetailDto>> getStudentBoardDetail(
            @PathVariable Long boardIdx,
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userIdx = jwtTokenProvider.getUserIdxFromToken(token);

        StudentBoardDetailDto studentBoardDetail = studentBoardDetailService.getStudentBoardDetail(boardIdx, userIdx);
        return ResponseEntity.ok(BaseResponse.ofSuccess(200, studentBoardDetail));
    }
}
