package com.example.campusfinder.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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

    private Long postOwnerId;  // 게시글 작성자 ID
    private Long userId;       // 채팅을 요청한 사용자 ID
    private Long boardIdx;     // 게시글 번호

    private String postOwnerCategory; // 게시글 작성자의 카테고리 (의뢰 또는 고용)
    private String userCategory;      // 채팅을 요청한 사용자의 카테고리 (의뢰 또는 고용)

    private String name; // 채팅방 이름

    // 기존 필드를 이용한 생성자
    public ChatRoom(Long postOwnerId, Long userId, Long boardIdx, String postOwnerCategory, String userCategory) {
        this.postOwnerId = postOwnerId;
        this.userId = userId;
        this.boardIdx = boardIdx;
        this.postOwnerCategory = postOwnerCategory;
        this.userCategory = userCategory;
        this.name = "채팅방 - " + postOwnerId;  // 채팅방 이름 자동 생성
    }
}
