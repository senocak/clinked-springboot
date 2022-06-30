package com.github.senocak.clinked.service;

import com.github.senocak.clinked.dto.auth.RoleResponse;
import com.github.senocak.clinked.dto.auth.UserResponse;
import com.github.senocak.clinked.dto.article.ArticleDto;
import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.Role;
import com.github.senocak.clinked.entity.User;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;

public class DtoConverter {
    private DtoConverter(){}

    public static ArticleDto convertEntityToDto(Article article){
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(article.getTitle());
        articleDto.setSlug(article.getSlug());
        articleDto.setContent(article.getContent());
        articleDto.setAuthor(article.getAuthor());
        articleDto.setUser(convertEntityToDto(article.getUser()));
        articleDto.setPublish(ZonedDateTime.parse(article.getPublish()).toInstant());
        articleDto.setCreatedAt(convertDateToLong(article.getCreatedAt()));
        articleDto.setUpdatedAt(convertDateToLong(article.getUpdatedAt()));
        return articleDto;
    }

    public static UserResponse convertEntityToDto(User user){
        return new UserResponse(
                user.getName(),
                user.getEmail(),
                user.getUsername(),
            user.getRoles().stream().map(DtoConverter::convertEntityToDto).collect(Collectors.toSet())
        );
    }

    public static RoleResponse convertEntityToDto(Role role){
        return new RoleResponse(role.getName());
    }

    public static long convertDateToLong(Date date){
        return date.getTime() / 1000;
    }
}
