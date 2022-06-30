package com.github.senocak.clinked.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.senocak.clinked.dto.BaseDto;

public record ArticleWrapperDto(
    @JsonProperty("article") ArticleDto articleDto
) implements BaseDto {}