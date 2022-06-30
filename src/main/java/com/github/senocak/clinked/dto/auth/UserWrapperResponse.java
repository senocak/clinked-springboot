package com.github.senocak.clinked.dto.auth;

import com.github.senocak.clinked.dto.BaseDto;

public record UserWrapperResponse (String token) implements BaseDto {}