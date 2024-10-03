package com.example.campusfinder.board.student.repository;

import com.example.campusfinder.board.student.entity.StudentBoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.example.campusfinder.board.student.repository
 * fileName       : StudentBoardImageRepository
 * author         : tlswl
 * date           : 2024-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-04        tlswl       최초 생성
 */
public interface StudentBoardImageRepository extends JpaRepository<StudentBoardImage, Long> {
}