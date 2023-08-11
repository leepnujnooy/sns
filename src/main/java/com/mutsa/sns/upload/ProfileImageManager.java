package com.mutsa.sns.upload;

import com.mutsa.sns.exception.CustomException;
import com.mutsa.sns.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ProfileImageManager{



    public String upload(MultipartFile file, Long id) {
        //파일이 비어있을 경우 예외처리
        if(file.isEmpty()) throw new CustomException(ErrorCode.NO_FILE);

        //profile 담을 디렉터리 생성
        String profileDir = String.format("media/%s/","user_"+id);

        //파일 경로 위치 예외처리
        try{
            Files.createDirectories(Path.of(profileDir));
        }
        catch (IOException e){
            throw new CustomException(ErrorCode.FAILED_TO_UPLOAD);
        }

        //파일명, 확장자 , 파일경로 설정
        String originalFilename = file.getOriginalFilename();
        String[] splitFilename = originalFilename.split("\\.");
        String extension = splitFilename[splitFilename.length-1];
        String profilePath = profileDir + ("profile."+extension);

        //파일 저장 예외처리
        try{
            file.transferTo(Path.of(profilePath));
        } catch (IOException e){
            throw new CustomException(ErrorCode.FAILED_TO_UPLOAD);
        }
        return String.format("/static/%s/%s","user_"+id,"profile."+extension);
    }

    public boolean delete(String fileName, Long id) {
        return false;
    }

}
