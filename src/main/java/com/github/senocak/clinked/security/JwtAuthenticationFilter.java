package com.github.senocak.clinked.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.senocak.clinked.exception.RestExceptionHandler;
import com.github.senocak.clinked.service.UserService;
import com.github.senocak.clinked.util.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader(AppConstants.TOKEN_HEADER_NAME);
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AppConstants.TOKEN_PREFIX)) {
                String jwt = bearerToken.substring(7);
                try{
                    tokenProvider.validateToken(jwt);
                    String userName = tokenProvider.getUserNameFromJWT(jwt);
                    UserDetails userDetails = userService.loadUserByUsername(userName);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.trace("SecurityContext created");
                }catch (Exception exception){
                    ResponseEntity<Object> responseEntity = new RestExceptionHandler()
                            .handleAccessDeniedException(new RuntimeException(exception.getMessage()));

                    response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    return;
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context. Error:"+ ex);
        }
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        filterChain.doFilter(request, response);
        logger.info(request.getRemoteAddr());
    }
}
