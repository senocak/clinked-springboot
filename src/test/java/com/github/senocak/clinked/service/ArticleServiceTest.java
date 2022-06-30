package com.github.senocak.clinked.service;

import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.factory.ArticleFactory;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.repository.ArticleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {
    @InjectMocks private ArticleService articleService;
    @Mock ArticleRepository articleRepository;

    @Test
    public void given_whenGetAllByUser_thenAssertResult() {
        // Given
        Article article = ArticleFactory.createArticle();
        List<Article> list = List.of(article);
        Page<Article> articles = new PageImpl<>(list);
        Mockito.doReturn(articles).when(articleRepository)
                .findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class));
        // When
        Page<Article> getAllByUser = articleService.getAllByUser(UserFactory.createUser(), 1, 1);
        // Assert
        assertNotNull(getAllByUser);
        assertNotNull(getAllByUser.getContent());
        assertEquals(1, getAllByUser.getContent().size());
        assertEquals(1, getAllByUser.getTotalElements());
    }

    @Test
    public void given_whenFindArticleBySlugOrId_thenAssertResult() {
        // Given
        Article article = ArticleFactory.createArticle();
        Mockito.doReturn(Optional.of(article)).when(articleRepository)
                .findArticleBySlugOrId(article.getSlug());
        // When
        Article findArticleBySlugOrId = articleService.findArticleBySlugOrId(article.getSlug());
        // Assert
        assertNotNull(findArticleBySlugOrId);
        assertEquals(article.getTitle(), findArticleBySlugOrId.getTitle());
    }

    @Test
    public void given_whenPersist_thenAssertResult() {
        // Given
        Article article = ArticleFactory.createArticle();
        Mockito.doReturn(article).when(articleRepository).save(article);
        // When
        Article findArticleBySlugOrId = articleService.persist(article);
        // Assert
        assertNotNull(findArticleBySlugOrId);
        assertEquals(article.getTitle(), findArticleBySlugOrId.getTitle());
    }

    @Test
    public void given_whenDelete_thenAssertResult() {
        // Given
        Article article = ArticleFactory.createArticle();
        // When
        articleService.delete(article);
        // Assert
        Mockito.verify(articleRepository).delete(article);
    }
}
