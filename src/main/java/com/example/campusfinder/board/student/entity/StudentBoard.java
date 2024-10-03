package com.example.campusfinder.board.student.entity;

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

    private String thumbnailImage;

    //true면 주변 근처 o, false면 주변 근처 x
    private boolean isNearCampus;

    @Enumerated(EnumType.STRING)
    private MeetingType meetingType;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Lob
    @Column(name="content",nullable = false)
    private String content;

    @OneToMany(mappedBy = "studentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentBoardImage> images = new ArrayList<>();

    // 이미지 추가 메서드
    public void addImage(StudentBoardImage image) {
        this.images.add(image);
        image.setStudentBoard(this);
    }

}
