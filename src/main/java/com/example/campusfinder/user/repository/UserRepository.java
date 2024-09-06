package com.example.campusfinder.user.repository;

import com.example.campusfinder.user.entity.UserEntity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName    : com.example.campusfinder.user
 * fileName       : UserRepository
 * author         : tlswl
 * date           : 2024-08-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-20        tlswl       최초 생성
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
}
