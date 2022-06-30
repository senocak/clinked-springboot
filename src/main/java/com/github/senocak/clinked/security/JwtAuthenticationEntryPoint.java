package com.github.senocak.clinked.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senocak.clinked.exception.RestExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        log.error("Responding with unauthorized error. Message: {}", ex.getMessage());

        ResponseEntity<Object> responseEntity = new RestExceptionHandler()
                .handleAccessDeniedException(new RuntimeException(ex.getMessage()));

        response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
    }
}
