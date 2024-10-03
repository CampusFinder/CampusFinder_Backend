package com.example.campusfinder.board.request.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * RequestBoardImage 엔티티: RequestBoard와 연관된 이미지 정보를 관리하는 엔티티.
 */
@Entity
@Table(name = "REQUEST_BOARD_IMAGE")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestBoardImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    // RequestBoard와 다대일 관계 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_idx")
    private RequestBoard requestBoard;

    public void setRequestBoard(RequestBoard requestBoard) {
        this.requestBoard = requestBoard;
    }
}
