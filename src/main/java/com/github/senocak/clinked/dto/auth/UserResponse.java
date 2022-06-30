package com.github.senocak.clinked.dto.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.senocak.clinked.dto.BaseDto;
import java.util.Set;

@JsonPropertyOrder({"name", "username", "email", "roles"})
public record UserResponse(
    String name,
    String email,
    String username,
    Set<RoleResponse> roles
) implements BaseDto {}