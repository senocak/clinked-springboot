package com.github.senocak.clinked.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senocak.clinked.dto.auth.LoginRequest;
import com.github.senocak.clinked.exception.RestExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@Tag("integration")
public class AuthControllerIT {
    @Autowired AuthController authController;
    @Autowired ObjectMapper objectMapper;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(RestExceptionHandler.class)
                .build();
    }

    @Test
    public void given_whenLogin_thenServerAssertResult() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest("anil1", "anil1");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(AuthController.URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest));
        // When
        ResultActions perform = mockMvc.perform(requestBuilder);
        // Assert
        perform.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
