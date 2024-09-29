package com.example.campusfinder.home.repository;

import com.example.campusfinder.home.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * packageName    : com.example.campusfinder.home.repository
 * fileName       : CategoryRepository
 * author         : tlswl
 * date           : 2024-09-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-30        tlswl       최초 생성
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
