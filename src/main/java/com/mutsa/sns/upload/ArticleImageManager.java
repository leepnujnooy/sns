package com.mutsa.sns.upload;

import com.mutsa.sns.exception.CustomException;
import com.mutsa.sns.exception.ErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class ArticleImageManager{

    public String upload(MultipartFile file, Long articleId, Long userId) {

        //Article 담을 디렉터리 생성
        String articleDir = String.format("media/%s/%s/","user_"+userId,"article_"+articleId);

        //만약에 articleDir 이 존재하지않는다면
        if(!Files.exists(Path.of(articleDir))){
            //파일 경로 위치 예외처리
            try{
                Files.createDirectories(Path.of(articleDir));
            }
            catch (IOException e){
                throw new CustomException(ErrorCode.FAILED_TO_UPLOAD);
            }
        }



        //파일명, 확장자 , 파일경로 설정
        String originalFilename = file.getOriginalFilename();
        String[] splitFilename = originalFilename.split("\\.");
        String extension = splitFilename[splitFilename.length-1];
        UUID uuid = UUID.randomUUID();
        String profilePath = articleDir + (uuid+"."+extension);


        //파일 저장 예외처리
        try{
            file.transferTo(Path.of(profilePath));
        } catch (IOException e){
            throw new CustomException(ErrorCode.FAILED_TO_UPLOAD);
        }
        return String.format("/static/%s/%s/%s","user_"+userId,"article_"+articleId,uuid+extension);
    }

    public String uploadDefault(Long articleId, Long userId){
        String articleDir = String.format("media/%s/%s/","user_"+userId,"article_"+articleId);
        try{
            Files.createDirectories(Path.of(articleDir));
        }
        catch(IOException e){
            throw new CustomException(ErrorCode.FAILED_TO_UPLOAD);
        }


        return String.format("/static/%s","default.png");
    }

    public void delete(String fileName,Long articleId ,Long userId) {

        //아티클 이미지
        String articleDir = String.format("media/%s/%s/","user_"+userId,"article_"+articleId);

        try{
            Files.delete(Path.of(articleDir));
        }
        catch(IOException e){
            throw new CustomException(ErrorCode.NO_FILE);
        }
    }
}
