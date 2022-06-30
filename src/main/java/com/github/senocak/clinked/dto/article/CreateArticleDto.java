package com.github.senocak.clinked.dto.article;

import com.github.senocak.clinked.dto.BaseDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateArticleDto(
    @NotNull @Size(min = 4, max = 100) String title,
    @NotNull @Size(min = 3) String content,
    @NotNull @Size(min = 1) String author,
    @NotNull @Size(min = 1) String publish
) implements BaseDto {}
