package com.github.senocak.clinked.dto.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.senocak.clinked.dto.BaseDto;
import java.util.List;

@JsonPropertyOrder({"article", "next", "total"})
public record ArticlesDto(
    @JsonProperty("article") List<ArticleDto> articleDto,
    long total,
    long next
) implements BaseDto {}