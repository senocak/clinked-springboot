package com.github.senocak.clinked.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.senocak.clinked.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank
    @Size(min = 3, max = 30)
    @JsonProperty("username")
    String username,

    @NotBlank
    @Size(min = 3, max = 20)
    String password
) implements BaseDto {}