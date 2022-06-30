package com.github.senocak.clinked.dto.auth;

import com.github.senocak.clinked.dto.BaseDto;
import com.github.senocak.clinked.util.AppConstants;

public record RoleResponse(AppConstants.RoleName name) implements BaseDto {}