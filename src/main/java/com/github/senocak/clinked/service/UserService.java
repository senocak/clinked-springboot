package com.github.senocak.clinked.service;

import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public User findByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email"));
    }

    public org.springframework.security.core.userdetails.User create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    @Transactional
    public org.springframework.security.core.userdetails.User loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = findByUsername(username);
        return create(user);
    }

    public User loggedInUser() throws ServerException {
        String username = ((org.springframework.security.core.userdetails.User)SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getUsername();
        return userRepository.findByUsername(username).orElseThrow(() ->
            new ServerException(new String[]{"User", "username", username}, HttpStatus.NOT_FOUND));
    }
}
