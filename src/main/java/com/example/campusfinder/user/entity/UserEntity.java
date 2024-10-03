package com.example.campusfinder.user.entity;

import com.example.campusfinder.core.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * packageName    : com.example.campusfinder.user.entity
 * fileName       : UserEntity
 * author         : tlswl
 * date           : 2024-08-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-08-19        tlswl       최초 생성
 */

@Entity
@Table(name="USER")
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Builder
public class UserEntity extends BaseEntity {

    @Id
    @Column(name="user_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    //휴대폰 번호
    @Column(name="phone_number", nullable = false)
    private String phoneNum;

    //비밀번호
    @Column(name="password", nullable = false)
    private String password;

    //교수, 학생 역할
    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    //학교 이메일
    @Column(name="email", nullable = false)
    private String email;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(name="univName", nullable = false)
    private String univName;

    @Column(name="email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name="phone_verified", nullable = false)
    private boolean phoneVerified=false;

    // 기본적으로 NULL 값을 가질 수 있는 프로필 이미지 URL 필드 추가
    @Column(name = "profile_image_url", nullable = true)
    private String profileImageUrl;

    //이메일 인증 성공 시 호출
    public void verifyEmail(){
        this.emailVerified=true;
    }

    public void verifyPhone(){
        this.phoneVerified=true;
    }


    //비밀번호 찾기 시에 사용하는 update 메서드
    // 비밀번호 찾기 시에 사용하는 update 메서드
    public UserEntity updatePassword(String password) {
        this.password = password;
        return this;
    }
}
