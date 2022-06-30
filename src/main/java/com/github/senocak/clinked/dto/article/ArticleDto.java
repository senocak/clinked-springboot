package com.github.senocak.clinked.dto.article;

import com.github.senocak.clinked.dto.BaseDto;
import com.github.senocak.clinked.dto.auth.UserResponse;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class ArticleDto implements BaseDto {
    private String title;
    private String slug;
    private String content;
    private String author;
    private UserResponse user;
    private Instant publish;
    private Long createdAt;
    private Long updatedAt;
}