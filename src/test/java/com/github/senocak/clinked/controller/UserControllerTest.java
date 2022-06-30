package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.dto.auth.UserResponse;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks private UserController userController;
    @Mock UserService userService;

    @Test
    public void given_whenMe_thenServerException() throws ServerException {
        // Given
        User user = UserFactory.createUser();
        Mockito.doReturn(user).when(userService).loggedInUser();
        // When
        ResponseEntity<UserResponse> login = userController.me();
        // Assert
        assertNotNull(login);
        assertNotNull(login.getBody());
        assertEquals(HttpStatus.OK, login.getStatusCode());
    }
}
