package com.mutsa.sns.service;

import com.mutsa.sns.dto.article.ArticleDetailResponse;
import com.mutsa.sns.dto.article.ArticleResponse;
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
            ArticleImage articleImage = articleImageRepository.save(ArticleImage
                    .builder()
                    .imageUrl(articleImageManager.uploadDefault(article.getId(),user.getId()))
                    .article(article)
                    .build());

            //대표이미지 default.png 로 저장
            article.setThumbnail(articleImage);
            articleRepository.save(article);

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
        for (int i = 0; i < files.length; i++) {
            //업로드 후 url 값 반환
            String imageUrl = articleImageManager.upload(files[i],article.getId(), user.getId());

            //ArticleImage 객체 생성
            ArticleImage articleImage = ArticleImage
                    .builder()
                    .article(article)
                    .imageUrl(imageUrl)
                    .build();

            //ArticleImage 객체 저장
            articleImageRepository.save(articleImage);

            //첫번째 이미지라면, 대표이미지로 저장
            if(i==0) {
                article.setThumbnail(articleImage);
                articleRepository.save(article);
            }
        }




    }



    //단독 피드 조회하기 ( 작성한 사용자 기준으로, 목록 형태의 조회가 가능 )
    public ArticleDetailResponse getOneArticle(Long articleId){
        Article article = findService.findByArticleId(articleId);
        List<ArticleImage> images = findService.findAllByArticleId(articleId);

        List<String> imageUrls = new ArrayList<>();
        for (ArticleImage image : images){
            imageUrls.add(image.getImageUrl());
        }

        return ArticleDetailResponse.builder()
                .title(article.getTitle())
                .content(article.getContent())
                .imageUrls(imageUrls)
                .build();
    }


    //전체 피드 조회하기
//    public List<ArticleResponse> getAllArticle(){
//        List<ArticleResponse> responseList = new ArrayList<>();
//
//        return ;
//    }

}
