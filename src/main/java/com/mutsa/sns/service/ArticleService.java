package com.mutsa.sns.service;

import com.mutsa.sns.dto.article.ArticleDetailResponseDto;
import com.mutsa.sns.entity.Article;
import com.mutsa.sns.entity.ArticleImage;
import com.mutsa.sns.entity.User;
import com.mutsa.sns.repository.ArticleImageRepository;
import com.mutsa.sns.repository.ArticleRepository;
import com.mutsa.sns.upload.ArticleImageManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;
    private final ArticleImageManager articleImageManager;
    private final FindService findService;


    //아티클 생성
    public void createArticle(MultipartFile[] files,String title,String content,String username){
        //유저 객체 가져오기
        User user = findService.findByUsername(username);
        log.info(user.getUsername());
        log.info(String.valueOf(files.length));
        log.info(String.valueOf(files[0]));
        log.info(files[0].getOriginalFilename());

        //만약 사진이 없는 Article 이라면? > draft 는 true
        //MultipartFile 을 form-data 로 보낼경우,
        //아무런 파일을 넣지 않아도 빈 string 값을 넣어보내게됨. 그래서 files.length 는 0이 될수없음
        if(files[0].isEmpty()){
            Article article = articleRepository.save(Article
                    .builder()
                    .title(title)
                    .content(content)
                    .user(user)
                    .draft(true)
                    .build());
            log.info("아티클 저장");

            //default 이미지 저장
            articleImageRepository.save(ArticleImage
                    .builder()
                    .imageUrl(articleImageManager.uploadDefault(article.getId(),user.getId()))
                    .article(article)
                    .build());
            log.info("기본이미지 저장");
            //메서드 종료
            return;
        }

        //Article 에 정보 담기.
        Article newArticle = Article
                .builder()
                .title(title)
                .content(content)
                .user(user)
                .draft(false)
                .build();

        //Article 저장하고 객체 가져오기
        Article article = articleRepository.save(newArticle);


        //ArticleImage 객체 생성
        for (MultipartFile image : files){
            //업로드 후 url 값 반환
            String imageUrl = articleImageManager.upload(image, article.getId(), user.getId());

            //articleImage 객체 생성
            ArticleImage articleImage = ArticleImage
                    .builder()
                    .article(article)
                    .imageUrl(imageUrl)
                    .build();

            //articleImage 객체 저장
            articleImageRepository.save(articleImage);
        }
    }



    //단독 피드 조회하기 ( 작성한 사용자 기준으로, 목록 형태의 조회가 가능 )
    public ArticleDetailResponseDto getOneArticle(Long articleId){
        Article article = findService.findByArticleId(articleId);
        List<ArticleImage> images = findService.findAllByArticleId(articleId);

        List<String> imageUrls = new ArrayList<>();
        for (ArticleImage image : images){
            imageUrls.add(image.getImageUrl());
        }

        return ArticleDetailResponseDto.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .imageUrls(imageUrls)
                .build();
    }



}
