package com.example.campusfinder.board.request.repository;

import com.example.campusfinder.board.request.entity.RequestBoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.example.campusfinder.board.request.repository
 * fileName       : RequestBoardImageRepository
 * author         : tlswl
 * date           : 2024-10-04
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-04        tlswl       최초 생성
 */
public interface RequestBoardImageRepository extends JpaRepository<RequestBoardImage, Long> {
}
