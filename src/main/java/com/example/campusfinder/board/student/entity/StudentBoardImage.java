package com.example.campusfinder.board.student.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName    : com.example.campusfinder.board.student.entity
 * fileName       : StudentBoardImage
 * author         : tlswl
 * date           : 2024-10-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl       최초 생성
 */
@Entity
@Table(name = "student_board_image")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
public class StudentBoardImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_idx")
    private Long imageIdx;

    @Column(name="image_url", nullable = true)
    private String imageUrl;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board_idx")
    private StudentBoard studentBoard;

    public void setStudentBoard(StudentBoard studentBoard){
        this.studentBoard=studentBoard;
    }
}
