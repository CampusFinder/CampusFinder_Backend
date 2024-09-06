package com.example.campusfinder.user.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.user.dto.request.signup.SignUpRequestDto;
import com.example.campusfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.campusfinder.user.controller
 * fileName       : SignUpController
 * author         : tlswl
 * date           : 2024-09-06
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-06        tlswl       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signup")
public class SignUpController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse> signUp(@RequestBody SignUpRequestDto signUpRequest){
        userService.signUpUser(signUpRequest);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(),"회원가입이 완료되었습니다."));
    }
}
