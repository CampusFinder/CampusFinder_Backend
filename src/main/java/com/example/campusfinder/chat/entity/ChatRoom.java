package com.example.campusfinder.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "chat_room")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private Long postOwnerId;
    private Long userId;  // 채팅방에 참여하는 사용자의 ID
    private Long boardIdx; // 게시글 번호 추가

    private String name;

    public ChatRoom(Long postOwnerId, Long userId, Long boardIdx) {
        this.postOwnerId = postOwnerId;
        this.userId = userId;
        this.boardIdx = boardIdx;
        this.name = "채팅방 - " + postOwnerId;
    }
}
