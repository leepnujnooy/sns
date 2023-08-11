package com.mutsa.sns.service;

import com.mutsa.sns.entity.Article;
import com.mutsa.sns.entity.ArticleImage;
import com.mutsa.sns.entity.User;
import com.mutsa.sns.exception.CustomException;
import com.mutsa.sns.exception.ErrorCode;
import com.mutsa.sns.repository.ArticleImageRepository;
import com.mutsa.sns.repository.ArticleRepository;
import com.mutsa.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class FindService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final ArticleImageRepository articleImageRepository;


    //DB 안에 유저아이디가 이미 존재하는지 찾아주는 메서드
    public void checkUserDuplication(String username){
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isPresent())throw new CustomException(ErrorCode.DUPLICATED_USER_NAME);
    }


    //유저 인지 아닌지 찾아주는 메서드
    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(()->new CustomException(ErrorCode.USER_NAME_NOT_FOUND));
    }

    //존재하는 article인지 확인해주는 메서드
    public Article findByArticleId(Long articleId){
        return articleRepository.findById(articleId)
                .orElseThrow(()->new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }

    //article 에 포함된 image 들을 가져오는 메서드
    public List<ArticleImage> findAllByArticleId(Long articleId){
        return articleImageRepository.findAllByArticle_Id(articleId)
                .orElseThrow(()->new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
    }
}
