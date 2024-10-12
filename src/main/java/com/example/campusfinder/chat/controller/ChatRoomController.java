package com.example.campusfinder.chat.controller;

import com.example.campusfinder.chat.dto.request.ChatRoomRequestDto;
import com.example.campusfinder.chat.dto.response.ChatRoomResponseDto;
import com.example.campusfinder.chat.service.ChatRoomService;
import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.core.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "채팅방 API", description = "채팅방 생성 및 관리 API")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "채팅방 생성", description = "게시글 작성자와 채팅방을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "채팅방 생성 성공", content = @Content(schema = @Schema(implementation = ChatRoomResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(schema = @Schema(implementation = BaseResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    })
    @PostMapping("/room/create")
    public ResponseEntity<BaseResponse<ChatRoomResponseDto>> createChatRoom(
            @Parameter(description = "게시글 작성자의 닉네임, 게시글 번호, 게시글 타입", required = true)
            @RequestBody ChatRoomRequestDto requestDto,
            HttpServletRequest request) {

        try {
            // 현재 로그인한 사용자 정보는 토큰에서 가져옴
            String token = jwtTokenProvider.resolveToken(request);
            Long currentUserId = jwtTokenProvider.getUserIdxFromToken(token);

            // Service로 데이터 전달하여 채팅방 생성
            ChatRoomResponseDto chatRoom = chatRoomService.createChatRoom(
                    requestDto.postOwnerNickName(),
                    currentUserId,
                    requestDto.boardIdx(),
                    requestDto.boardType()  // boardType 추가
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ofSuccess(HttpStatus.CREATED.value(), chatRoom));
        } catch (IllegalArgumentException e) {
            // 예외 발생 시 프론트에 메시지 전달
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BaseResponse.ofFail(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponse.ofError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 오류가 발생했습니다."));
        }
    }
}
