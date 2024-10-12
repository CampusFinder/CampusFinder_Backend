package com.example.campusfinder.chat.service;

import com.example.campusfinder.board.coinstant.BoardType;
import com.example.campusfinder.board.request.entity.RequestBoard;
import com.example.campusfinder.board.request.repository.RequestBoardRepository;
import com.example.campusfinder.board.student.entity.StudentBoard;
import com.example.campusfinder.board.student.repository.StudentBoardRepository;
import com.example.campusfinder.chat.dto.response.ChatRoomResponseDto;
import com.example.campusfinder.chat.entity.ChatRoom;
import com.example.campusfinder.chat.repository.ChatRoomRepository;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final StudentBoardRepository studentBoardRepository;
    private final RequestBoardRepository requestBoardRepository;


    @Transactional
    public ChatRoomResponseDto createChatRoom(String postOwnerNickName, Long currentUserId, Long boardIdx, String boardType) {
        // 게시글 작성자 닉네임으로 사용자 찾기
        UserEntity postOwner = userRepository.findByNickname(postOwnerNickName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다."));

        // 현재 사용자와 게시글 작성자가 동일한 경우 예외 처리
        if (postOwner.getUserIdx().equals(currentUserId)) {
            throw new IllegalArgumentException("자기 자신과는 채팅방을 생성할 수 없습니다.");
        }

        // 기존 채팅방이 존재하는지 확인
        ChatRoom existingChatRoom = chatRoomRepository.findByPostOwnerIdAndUserIdAndBoardIdx(postOwner.getUserIdx(), currentUserId, boardIdx);

        if (existingChatRoom != null) {
            // 이미 채팅방이 존재할 경우, 해당 채팅방 정보를 반환
            return new ChatRoomResponseDto(existingChatRoom.getChatRoomId(), existingChatRoom.getName());
        }

        // 카테고리 설정
        String postOwnerCategory;
        String userCategory;

        // request로 전달받은 boardType에 따라 분기
        if ("REQUEST".equalsIgnoreCase(boardType)) {
            postOwnerCategory = "고용";
            userCategory = "의뢰";
        } else if ("STUDENT".equalsIgnoreCase(boardType)) {
            postOwnerCategory = "의뢰";
            userCategory = "고용";
        } else {
            throw new IllegalArgumentException("유효하지 않은 게시글 타입입니다.");
        }

        // 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(postOwner.getUserIdx(), currentUserId, boardIdx, postOwnerCategory, userCategory);
        chatRoomRepository.save(chatRoom);

        return new ChatRoomResponseDto(chatRoom.getChatRoomId(), chatRoom.getName());
    }

}