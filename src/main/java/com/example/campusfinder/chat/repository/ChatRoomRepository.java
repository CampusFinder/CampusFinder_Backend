package com.example.campusfinder.chat.repository;


import com.example.campusfinder.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}

