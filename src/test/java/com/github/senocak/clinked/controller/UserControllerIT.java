package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.RestExceptionHandler;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.service.UserService;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@Tag("integration")
public class UserControllerIT {
    @Autowired UserController userController;
    @MockBean UserService userService;
    MockMvc mockMvc;
    User user;

    @BeforeEach
    void beforeEach() throws ServerException {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
        user = UserFactory.createUser();
        Mockito.doReturn(user).when(userService).loggedInUser();
    }

    @Test
    public void given_whenLogin_thenServerAssertResult() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(UserController.URL + "/me");
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name",
                IsEqual.equalTo(user.getName())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.username",
                IsEqual.equalTo(user.getUsername())))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                IsEqual.equalTo(user.getEmail())));
    }
}
