package com.example.campusfinder.board.request.entity;

import com.example.campusfinder.board.coinstant.BoardType;
import com.example.campusfinder.board.coinstant.MeetingType;
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
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private BoardType boardType;  // 의뢰인지 고용인지 구분

    @Lob
    @Column(name="content",nullable = false)
    private String content;

    @Column(name = "nickname")
    private String nickname;

    private String thumbnailImage;
    private Boolean isUrgent;

    private Integer money;
    @Column(name = "deadline")
    private LocalDate deadline; // 마감 기한

    private Boolean isNegotiable; // 합의가능

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Column(name = "view_count")
    private int viewCount = 0; // 조회수 초기값을 0으로 설정

    public void incrementViewCount() {
        this.viewCount += 1;
    }

    // 이미지 목록 추가
    @OneToMany(mappedBy = "requestBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestBoardImage> images = new ArrayList<>();

    // 이미지를 추가하는 메서드
    public void addImage(RequestBoardImage image) {
        images.add(image);
        image.setRequestBoard(this);
    }

    // 이미지를 삭제하는 메서드
    public void removeImage(RequestBoardImage image) {
        images.remove(image);
        image.setRequestBoard(null);
    }
    //
}
