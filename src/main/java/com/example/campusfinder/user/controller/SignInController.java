package com.example.campusfinder.user.controller;

import com.example.campusfinder.core.base.BaseResponse;
import com.example.campusfinder.user.dto.request.signin.SignInRequestDto;
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
 * fileName       : SignInController
 * author         : tlswl
 * date           : 2024-09-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-09-07        tlswl       최초 생성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/signin")
public class SignInController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse> singIn(@RequestBody SignInRequestDto signInRequestDto){
        String accessToken = userService.SignInUser(signInRequestDto);
        return ResponseEntity.ok(BaseResponse.ofSuccess(HttpStatus.OK.value(),accessToken));
    }
}
