package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.dto.article.ArticleWrapperDto;
import com.github.senocak.clinked.dto.article.ArticlesDto;
import com.github.senocak.clinked.dto.article.CreateArticleDto;
import com.github.senocak.clinked.dto.article.UpdateArticleDto;
import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.Role;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.factory.ArticleFactory;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.service.ArticleService;
import com.github.senocak.clinked.service.UserService;
import com.github.senocak.clinked.util.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTest {
    @InjectMocks private ArticleController articleController;
    @Mock UserService userService;
    @Mock ArticleService articleService;
    User user;

    @BeforeEach
    public void setup() throws ServerException {
        user = UserFactory.createUser();
        Mockito.lenient().doReturn(user).when(userService).loggedInUser();
        Mockito.lenient().doReturn(user).when(userService).findByUsername(Mockito.anyString());
    }

    @Nested
    class CreteTest {
        @Test
        public void givenExistArticle_whenCreate_thenServerException() {
            // Given
            CreateArticleDto createArticleDto = new CreateArticleDto("title", "content", "author", "01.01.2022");
            Article article = ArticleFactory.createArticle();
            Mockito.doReturn(article).when(articleService).findArticleBySlugOrId(AppConstants.toSlug(createArticleDto.title()));
            // When
            Executable create = () -> articleController.create(createArticleDto);
            // Assert
            assertThrows(ServerException.class, create);
        }

        @Test
        public void given_whenCreate_thenAssertResult() throws ServerException {
            // Given
            CreateArticleDto createArticleDto = new CreateArticleDto("title", "content", "author", "01.01.2022");
            Article article = ArticleFactory.createArticle();
            Mockito.doReturn(article).when(articleService).persist(Mockito.any(Article.class));
            // When
            ResponseEntity<ArticleWrapperDto> create = articleController.create(createArticleDto);
            // Assert
            assertEquals(HttpStatus.CREATED, create.getStatusCode());
            assertNotNull(create.getBody());
            assertNotNull(create.getBody().articleDto());
            assertEquals(article.getTitle(), create.getBody().articleDto().getTitle());
        }
    }

    @Nested
    class GetAllTest {
        @Test
        public void given_whenGetAll_thenAssertResult() {
            // Given
            Article article = ArticleFactory.createArticle();
            List<Article> list = List.of(article);
            Page<Article> articles = new PageImpl<>(list);
            Mockito.doReturn(articles).when(articleService)
                    .getAllByUser(user, 1, 1);
            // When
            ResponseEntity<ArticlesDto> create = articleController.getAll(1, 1, "username");
            // Assert
            assertEquals(HttpStatus.OK, create.getStatusCode());
            assertNotNull(create.getBody());
            assertNotNull(create.getBody().articleDto());
            assertNotNull(create.getBody().articleDto().get(0));
            assertEquals(article.getTitle(), create.getBody().articleDto().get(0).getTitle());
            assertEquals(1, create.getBody().total());
            assertEquals(0, create.getBody().next());
        }
    }

    @Nested
    class GetSingleTest {
        @Test
        public void given_whenGetSingle_thenAssertResult() throws ServerException {
            // Given
            Article article = ArticleFactory.createArticle();
            Mockito.doReturn(article).when(articleService).findArticleBySlugOrId(article.getSlug());
            // When
            ResponseEntity<ArticleWrapperDto> create = articleController.getSingle(article.getSlug());
            // Assert
            assertEquals(HttpStatus.OK, create.getStatusCode());
            assertNotNull(create.getBody());
            assertNotNull(create.getBody().articleDto());
            assertEquals(article.getTitle(), create.getBody().articleDto().getTitle());
        }
    }

    @Nested
    class UpdateTest {
        @Test
        public void givenInvalidUser_whenUpdate_thenServerException() throws ServerException {
            // Given
            UpdateArticleDto updateArticleDto = new UpdateArticleDto("title", "content", "author");
            Article article = ArticleFactory.createArticle();
            Mockito.doReturn(article).when(articleService).findArticleBySlugOrId(article.getSlug());
            user.setEmail("invalid@mail.com");
            user.setRoles(Collections.singleton(Role.builder().name(AppConstants.RoleName.ROLE_USER).build()));
            Mockito.doReturn(user).when(userService).loggedInUser();
            // When
            Executable create = () -> articleController.update(article.getSlug(), updateArticleDto);
            // Assert
            assertThrows(ServerException.class, create);
        }

        @Test
        public void given_whenUpdate_thenAssertResult() throws ServerException {
            // Given
            UpdateArticleDto updateArticleDto = new UpdateArticleDto("title", "content", "author");
            Article article = ArticleFactory.createArticle();
            Mockito.doReturn(article).when(articleService).findArticleBySlugOrId(article.getSlug());
            Mockito.doReturn(article).when(articleService).persist(Mockito.any(Article.class));
            // When
            ResponseEntity<ArticleWrapperDto> create = articleController.update(article.getSlug(), updateArticleDto);
            // Assert
            assertEquals(HttpStatus.OK, create.getStatusCode());
            assertNotNull(create.getBody());
            assertNotNull(create.getBody().articleDto());
            assertEquals(article.getTitle(), create.getBody().articleDto().getTitle());
        }
    }

    @Nested
    class DeleteTest {
        @Test
        public void given_whenDelete_thenAssertResult() throws ServerException {
            // Given
            Article article = ArticleFactory.createArticle();
            Mockito.doReturn(article).when(articleService).findArticleBySlugOrId(article.getSlug());
            // When
            ResponseEntity<Void> create = articleController.delete(article.getSlug());
            // Assert
            assertEquals(HttpStatus.NO_CONTENT, create.getStatusCode());
        }
    }
}
