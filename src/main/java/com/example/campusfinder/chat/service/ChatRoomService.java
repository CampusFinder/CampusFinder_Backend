package com.example.campusfinder.chat.service;

import com.example.campusfinder.chat.dto.response.ChatRoomResponseDto;
import com.example.campusfinder.chat.entity.ChatRoom;
import com.example.campusfinder.chat.repository.ChatRoomRepository;
import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserRepository userRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ChatRoomResponseDto createChatRoom(String postOwnerNickName, Long currentUserId, Long boardIdx) {
        // 게시글 작성자 닉네임으로 사용자 찾기
        UserEntity postOwner = userRepository.findByNickname(postOwnerNickName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 닉네임입니다."));

        // 현재 사용자와 게시글 작성자가 동일한 경우 예외 처리
        if (postOwner.getUserIdx().equals(currentUserId)) {
            throw new IllegalArgumentException("자기 자신과는 채팅방을 생성할 수 없습니다.");
        }

        // 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(postOwner.getUserIdx(), currentUserId, boardIdx);
        chatRoomRepository.save(chatRoom);

        return new ChatRoomResponseDto(chatRoom.getChatRoomId(), chatRoom.getName());
    }
}