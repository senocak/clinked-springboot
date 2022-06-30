package com.github.senocak.clinked.service;

import com.github.senocak.clinked.dto.article.ArticleDto;
import com.github.senocak.clinked.dto.auth.RoleResponse;
import com.github.senocak.clinked.dto.auth.UserResponse;
import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.Role;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.factory.ArticleFactory;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.util.AppConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DtoConverterTest {
    @Test
    public void given_whenConvertEntityToDto_thenAssertResultArticleDto() {
        // Given
        Article article = ArticleFactory.createArticle();
        // When
        ArticleDto articleDto = DtoConverter.convertEntityToDto(article);
        // Assert
        assertNotNull(articleDto);
        assertEquals(article.getTitle(), articleDto.getTitle());
    }

    @Test
    public void given_whenConvertEntityToDto_thenAssertResultUserResponse() {
        // Given
        User user = UserFactory.createUser();
        // When
        UserResponse userResponse = DtoConverter.convertEntityToDto(user);
        // Assert
        assertNotNull(userResponse);
        assertEquals(userResponse.email(), userResponse.email());
    }

    @Test
    public void given_whenConvertEntityToDto_thenAssertResultRoleResponse() {
        // Given
        Role role = new Role();
        role.setName(AppConstants.RoleName.ROLE_ADMIN);
        // When
        RoleResponse roleResponse = DtoConverter.convertEntityToDto(role);
        // Assert
        assertNotNull(roleResponse);
        assertEquals(roleResponse.name(), roleResponse.name());
    }

    @Test
    public void given_whenConvertDateToLong_thenAssertResult() {
        // When
        long response = DtoConverter.convertDateToLong(new Date());
        // Assert
        assertNotNull(response);
    }
}
