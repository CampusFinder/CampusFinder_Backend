package com.example.campusfinder.board.student.entity;

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

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.example.campusfinder.board.Entity
 * fileName       : Board
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
@Entity
@Table(name = "STUDENT_BOARD")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class StudentBoard extends BaseEntity {

    @Id
    @Column(name="board_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @Lob
    @Column(name="title",nullable = false)
    private String title;

    @Column(name = "nickname")
    private String nickname;

    @Enumerated(EnumType.STRING)
    private BoardType boardType;  // 의뢰인지 고용인지 구분

    private String thumbnailImage;

    //true면 주변 근처 o, false면 주변 근처 x
    private Boolean isNearCampus;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Lob
    @Column(name="content",nullable = false)
    private String content;

    @Column(name = "view_count", nullable = false)
    private int viewCount=0; // 조회수 필드 추가

    @OneToMany(mappedBy = "studentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentBoardImage> images = new ArrayList<>();

    // 이미지 추가 메서드
    public void addImage(StudentBoardImage image) {
        this.images.add(image);
        image.setStudentBoard(this);
    }
    // 조회수 증가 메서드
    public void incrementViewCount() {
        this.viewCount += 1;
    }

}
