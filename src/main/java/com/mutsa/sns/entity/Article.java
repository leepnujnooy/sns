package com.mutsa.sns.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private boolean draft;
    private LocalDateTime deleted_at;

    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "article",fetch = FetchType.LAZY)
    private List<ArticleImage> articleImageList;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
