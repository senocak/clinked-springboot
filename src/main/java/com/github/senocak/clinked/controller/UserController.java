package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.dto.auth.UserResponse;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.service.DtoConverter;
import com.github.senocak.clinked.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(UserController.URL)
// updated controller
public class UserController {
    public static final String URL = "/api/v1/user";
    private final UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() throws ServerException {
        User user = userService.loggedInUser();
        UserResponse login = DtoConverter.convertEntityToDto(user);
        return ResponseEntity.ok(login);
    }
}
