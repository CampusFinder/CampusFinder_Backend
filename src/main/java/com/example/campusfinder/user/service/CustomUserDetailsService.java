package com.example.campusfinder.user.service;

import com.example.campusfinder.user.entity.UserEntity;
import com.example.campusfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * packageName    : com.example.campusfinder.core.security
 * fileName       : CustomUserDetailsService
 * author         : tlswl
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        tlswl       최초 생성
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByPhoneNum(phone)
                .orElseThrow(() -> new UsernameNotFoundException("맞는 회원을 찾을 수 없습니다."));

        return new org.springframework.security.core.userdetails.User(
                user.getPhoneNum(),
                user.getPassword(),
                Collections.emptyList() // 권한 관련 처리가 필요하다면 여기에 추가할 수 있습니다.
        );
    }
}
