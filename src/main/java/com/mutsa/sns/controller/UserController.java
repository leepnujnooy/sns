package com.mutsa.sns.controller;

import com.mutsa.sns.dto.ResponseDto;
import com.mutsa.sns.dto.user.LoginRequestDto;
import com.mutsa.sns.dto.user.SignupRequestDto;
import com.mutsa.sns.entity.User;
import com.mutsa.sns.exception.CustomException;
import com.mutsa.sns.exception.ErrorCode;
import com.mutsa.sns.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signupRequest(@RequestBody SignupRequestDto signupRequest){
        userService.createUser(signupRequest);
        return ResponseEntity.status(200).body(new ResponseDto("회원가입이 정상적으로 처리되었습니다."));
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> loginRequest(@RequestBody LoginRequestDto loginRequest, HttpServletResponse response){
        response.reset();
        response.setHeader(HttpHeaders.AUTHORIZATION, userService.doLogin(loginRequest));
        return ResponseEntity.status(200).body(new ResponseDto("로그인 되었습니다."));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateImageRequest(
            @RequestParam("image")MultipartFile file,
            Authentication authentication
            ){
        // 인증정보 안에 있는 Principal 객체를 User 로 형 변환후 이름 가져오기
        // 하지만 여기서는 username 밖에 가져오지못함. 토큰 필터에서 인증정보 생성할때 이름만 넣어줬기 때문임.
        User user = (User) authentication.getPrincipal();
        userService.uploadImage(file,user.getUsername());
        return ResponseEntity.status(200).body(new ResponseDto("성공적으로 업로드 되었습니다"));
    }
}
