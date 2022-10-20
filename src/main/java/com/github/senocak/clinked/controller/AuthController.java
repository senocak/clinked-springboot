package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.dto.auth.LoginRequest;
import com.github.senocak.clinked.dto.auth.UserResponse;
import com.github.senocak.clinked.dto.auth.UserWrapperResponse;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.security.JwtTokenProvider;
import com.github.senocak.clinked.service.DtoConverter;
import com.github.senocak.clinked.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(AuthController.URL)
public class AuthController {
    public static final String URL = "/api/v1/3";
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<UserWrapperResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByUsername(loginRequest.username());
        UserResponse login = DtoConverter.convertEntityToDto(user);
        return ResponseEntity.ok(generateUserWrapperResponse(login));
    }

    private UserWrapperResponse generateUserWrapperResponse(UserResponse userResponse) {
        List<String> roles = userResponse.roles().stream().map(roleResponse -> roleResponse.name().getRole())
                .collect(Collectors.toList());
        String generatedToken = tokenProvider.generateJwtToken(userResponse.username(), roles);
        return new UserWrapperResponse(generatedToken);
    }
}
