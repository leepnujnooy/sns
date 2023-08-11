package com.mutsa.sns.service;

import com.mutsa.sns.dto.user.LoginRequestDto;
import com.mutsa.sns.dto.user.SignupRequestDto;
import com.mutsa.sns.entity.User;
import com.mutsa.sns.exception.CustomException;
import com.mutsa.sns.exception.ErrorCode;
import com.mutsa.sns.repository.UserRepository;
import com.mutsa.sns.token.JwtTokenUtil;
import com.mutsa.sns.upload.ProfileImageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final ProfileImageManager profileImageManager;
    private final FindService findService;
    //유저 생성
    public void createUser(SignupRequestDto signupRequest){
        //Username 중복확인
        findService.checkUserDuplication(signupRequest.getUsername());

        //필수 입력 정보확인 로직 ( username , password )
        if(signupRequest.getUsername()==null ||
                signupRequest.getUsername().isEmpty() ||
                signupRequest.getPassword()==null ||
                signupRequest.getPassword().isEmpty()
        )
            throw new CustomException(ErrorCode.OMISSION_OF_ESSENTIAL);

        //생성
        User user = User
                .builder()
                .username(signupRequest.getUsername())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .email(signupRequest.getEmail())
                .phone(signupRequest.getPhone())
                .build();

        //DB 저장
        userRepository.save(user);
    }


    //유저 로그인 ( Token 발급은 서비스단에서 하는게 맞나.. ? )
    public String doLogin(LoginRequestDto loginRequest){
        //유저 검증
        User user = findService.findByUsername(loginRequest.getUsername());

        //비밀번호 검증
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.WRONG_PASSWORD);

        //토큰 발급
        return "Bearer " + jwtTokenUtil.createToken(user.getUsername());
    }



    //프로필사진 업데이트
    public void uploadImage(MultipartFile file , String username){
        //유저 객체 생성
        User user = findService.findByUsername(username);

        //사진 파일 업로드 후 경로 받아오기
        String imagePath = profileImageManager.upload(file,user.getId());
        log.info(imagePath);

        //경로 객체에 담기
        user.setProfile_img(imagePath);

        //사진 경로 DB에 저장
        userRepository.save(user);
    }

    //프로필사진 삭제
    public void deleteImage(String username){
        //유저 객체 생성
        User user = findService.findByUsername(username);

        //profile 이 기본 이미지가 아니라면 지우

    }
}
