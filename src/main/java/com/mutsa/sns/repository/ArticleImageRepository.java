package com.mutsa.sns.repository;

import com.mutsa.sns.entity.Article;
import com.mutsa.sns.entity.ArticleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleImageRepository extends JpaRepository<ArticleImage,Long> {
    Optional<List<ArticleImage>> findAllByArticle_Id(Long articleId);
}
