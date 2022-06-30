package com.github.senocak.clinked.factory;

import com.github.senocak.clinked.entity.Role;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.util.AppConstants;
import java.util.Collections;

public class UserFactory {
    public static User createUser() {
        User user = new User();
        user.setId(1);
        user.setName("anil");
        user.setUsername("anil");
        user.setEmail("anil@senocak.com");
        user.setPassword("anil");
        user.setRoles(Collections.singleton(Role.builder().name(AppConstants.RoleName.ROLE_ADMIN).build()));
        return user;
    }
}
