package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.dto.auth.LoginRequest;
import com.github.senocak.clinked.dto.auth.UserWrapperResponse;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.security.JwtTokenProvider;
import com.github.senocak.clinked.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    @InjectMocks private AuthController authController;
    @Mock UserService userService;
    @Mock JwtTokenProvider jwtTokenProvider;
    @Mock AuthenticationManager authenticationManager;

    @Test
    public void given_whenLogin_thenServerException() {
        // Given
        LoginRequest loginRequest = new LoginRequest("username", "password");
        User user = UserFactory.createUser();
        Mockito.doReturn(user).when(userService).findByUsername(loginRequest.username());
        // When
        ResponseEntity<UserWrapperResponse> login = authController.login(loginRequest);
        // Assert
        assertNotNull(login);
        assertNotNull(login.getBody());
        assertEquals(HttpStatus.OK, login.getStatusCode());
    }
}
