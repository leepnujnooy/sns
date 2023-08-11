package com.mutsa.sns.repository;

import com.mutsa.sns.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article,Long> {
    Optional<Article> findByUser_Id(Long userId);
}
