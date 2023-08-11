package com.mutsa.sns.controller;

import com.mutsa.sns.dto.ResponseDto;
import com.mutsa.sns.dto.article.ArticleDetailResponseDto;
import com.mutsa.sns.entity.User;
import com.mutsa.sns.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    //인가된 사용자만

    //게시글 생성
    @PostMapping
    public ResponseEntity<ResponseDto> createArticle(
            Authentication auth,
            @RequestParam("image")MultipartFile[] files,
            @RequestParam("title")String title,
            @RequestParam("content")String content){
        User user = (User) auth.getPrincipal();
        articleService.createArticle(files,title,content,user.getUsername());
        return ResponseEntity.status(200).body(new ResponseDto("피드 등록 완료"));
    }

    //게시글 단독 조회
    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleDetailResponseDto> getOneArticle(
            @PathVariable("articleId") Long articleId){
        return ResponseEntity.status(200).body(articleService.getOneArticle(articleId));
    }
}
