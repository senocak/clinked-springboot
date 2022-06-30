package com.github.senocak.clinked.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonPropertyOrder({"statusCode", "variables"})
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonTypeName("exception")
public record ExceptionDto(
    int statusCode,
    String[] variables
) implements BaseDto { }

