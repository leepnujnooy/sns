package com.mutsa.sns.dto.article;

import com.mutsa.sns.entity.ArticleImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailResponseDto {

    private String title;
    private String content;
    private List<String> imageUrls;

}
