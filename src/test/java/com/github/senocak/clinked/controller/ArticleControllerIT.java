package com.github.senocak.clinked.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senocak.clinked.dto.article.CreateArticleDto;
import com.github.senocak.clinked.dto.article.UpdateArticleDto;
import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.RestExceptionHandler;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.repository.ArticleRepository;
import com.github.senocak.clinked.repository.UserRepository;
import com.github.senocak.clinked.service.UserService;
import com.github.senocak.clinked.util.AppConstants;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;

@SpringBootTest
@Tag("integration")
public class ArticleControllerIT {
    @Autowired ArticleController articleController;
    @Autowired ObjectMapper objectMapper;
    @Autowired ArticleRepository articleRepository;
    @Autowired UserRepository userRepository;
    @MockBean UserService userService;
    MockMvc mockMvc;
    User user;

    @BeforeEach
    void beforeEach() throws ServerException {
        mockMvc = MockMvcBuilders.standaloneSetup(articleController)
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
        user = userRepository.findAll().stream().findFirst().get();
        Mockito.lenient().doReturn(user).when(userService).loggedInUser();
        articleRepository.deleteAll();
        Article article1 = Article.builder()
                .id(UUID.randomUUID().toString())
                .title("Article Title 1")
                .slug(AppConstants.toSlug("Article Title 1"))
                .content("Article Content 1")
                .author("Article Author 1")
                .publish("2010-01-01T12:00:00+01:00")
                .user(user)
                .build();
        articleRepository.save(article1);
    }

    @Test
    public void givenExistArticle_whenCreate_thenServerException() throws Exception {
        // Given
        CreateArticleDto createArticleDto = new CreateArticleDto("Article Title 1", "content",
                "author", "2010-01-01T12:00:00+01:00");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ArticleController.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(createArticleDto));
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform
            .andExpect(MockMvcResultMatchers.status().isConflict())
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception.statusCode",
                IsEqual.equalTo(HttpStatus.CONFLICT.value())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception.variables",
                Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.exception.variables[0]",
                IsEqual.equalTo("Article already exist")));
    }

    @Test
    public void given_whenCreate_thenAssertResult() throws Exception {
        // Given
        CreateArticleDto createArticleDto = new CreateArticleDto("lorem ipsum", "lorem ipsum",
                "lorem ipsum", "2010-01-01T12:00:00+01:00");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(ArticleController.URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(createArticleDto));
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.article.title",
                IsEqual.equalTo(createArticleDto.title())));
    }

    @Test
    public void given_whenGetAll_thenAssertResult() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ArticleController.URL);
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.article",
                Matchers.hasSize(1)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.next",
                IsEqual.equalTo(0)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.total",
                IsEqual.equalTo(1)));
    }

    @Test
    public void given_whenGetSingle_thenAssertResult() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(ArticleController.URL + "/" +
                AppConstants.toSlug("Article Title 1"));
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void given_whenUpdate_thenAssertResult() throws Exception {
        // Given
        UpdateArticleDto updateArticleDto = new UpdateArticleDto("lorem ipsum", "lorem ipsum",
                "lorem ipsum");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.patch(ArticleController.URL + "/" +
                        AppConstants.toSlug("Article Title 1"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValueAsString(updateArticleDto));
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.article.title",
                IsEqual.equalTo(updateArticleDto.title())));
    }

    @Test
    public void given_whenDelete_thenAssertResult() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(ArticleController.URL + "/" +
                        AppConstants.toSlug("Article Title 1"));
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private String writeValueAsString(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }
}
