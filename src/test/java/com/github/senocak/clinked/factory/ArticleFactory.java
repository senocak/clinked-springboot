package com.github.senocak.clinked.factory;

import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.util.AppConstants;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public class ArticleFactory {
    public static Article createArticle() {
        Article article = new Article();
        article.setId(UUID.randomUUID().toString());
        article.setTitle("Title");
        article.setSlug(AppConstants.toSlug("Title"));
        article.setAuthor("Author");
        article.setContent("Content");
        article.setPublish("2010-01-01T11:00:00Z");
        article.setCreatedAt(Date.from(Instant.now()));
        article.setUpdatedAt(Date.from(Instant.now()));
        article.setUser(UserFactory.createUser());
        return article;
    }
}
