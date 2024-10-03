package com.example.campusfinder.board.request.entity;

import com.example.campusfinder.board.student.entity.MeetingType;
import com.example.campusfinder.core.base.BaseEntity;
import com.example.campusfinder.home.entity.CategoryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

/**
 * packageName    : com.example.campusfinder.board.Entity
 * fileName       : RequestBoard
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
@Entity
@Table(name = "REQUEST_BOARD")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class RequestBoard extends BaseEntity {
    @Id
    @Column(name="board_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @Lob
    @Column(name="title",nullable = false)
    private String title;

    @Lob
    @Column(name="content",nullable = false)
    private String content;

    @Column(name = "nickname")
    private String nickname;

    private String thumbnailImage;
    private boolean isUrgent;

    private int money;
    @Column(name = "deadline")
    private LocalDate deadline; // 마감 기한

    private boolean isNegotiable; // 합의가능

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;
}
