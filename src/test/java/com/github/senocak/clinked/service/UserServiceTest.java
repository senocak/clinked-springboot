package com.github.senocak.clinked.service;

import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.factory.UserFactory;
import com.github.senocak.clinked.repository.UserRepository;
import com.github.senocak.clinked.util.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks private UserService userService;
    @Mock UserRepository userRepository;

    @BeforeEach
    void setup(){
        SecurityContext securityContext = new SecurityContextImpl();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AppConstants.RoleName.ROLE_USER.toString()));
        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User(
                "username", "password", authorities);
        securityContext.setAuthentication(new TestingAuthenticationToken(user, null, authorities));
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void givenUsername_whenFindByUsername_thenAssertResult() {
        // Given
        User user = UserFactory.createUser();
        Mockito.doReturn(Optional.of(user)).when(userRepository)
                .findByUsername("username");
        // When
        User findByUsername = userService.findByUsername("username");
        // Assert
        assertNotNull(findByUsername);
        assertEquals(findByUsername.getEmail(), user.getEmail());
    }

    @Test
    public void givenInvalidUsername_whenFindByUsername_thenAssertResult() {
        // Given
        Mockito.doReturn(Optional.empty()).when(userRepository)
                .findByUsername("username");
        // When
        Executable executable = () -> userService.findByUsername("username");
        // Assert
        assertThrows(UsernameNotFoundException.class, executable);
    }

    @Test
    public void givenUser_whenCreate_thenAssertResult() {
        // Given
        User user = UserFactory.createUser();
        // When
        org.springframework.security.core.userdetails.User findByUsername = userService.create(user);
        // Assert
        assertNotNull(findByUsername);
        assertEquals(findByUsername.getUsername(), user.getUsername());
    }

    @Test
    public void givenUser_whenLoggedInUser_thenAssertResult() throws ServerException {
        // Given
        User user = UserFactory.createUser();
        Mockito.doReturn(Optional.of(user)).when(userRepository).findByUsername("username");
        // When
        User loggedInUser = userService.loggedInUser();
        // Assert
        assertNotNull(loggedInUser);
        assertEquals(loggedInUser.getEmail(), user.getEmail());
    }

    @Test
    public void givenUser_whenLoggedInUser_thenServerException() {
        // Given
        Mockito.doReturn(Optional.empty()).when(userRepository).findByUsername("username");
        // When
        Executable executable = () -> userService.loggedInUser();
        // Assert
        assertThrows(ServerException.class, executable);
    }
}
