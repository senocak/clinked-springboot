package com.github.senocak.clinked.dto.article;

import com.github.senocak.clinked.dto.BaseDto;
import jakarta.validation.constraints.Size;

public record UpdateArticleDto(
    @Size(min = 4, max = 40) String title,
    @Size(min = 3) String content,
    @Size(min = 1) String author
) implements BaseDto {}