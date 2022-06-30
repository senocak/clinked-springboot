package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.dto.article.ArticleDto;
import com.github.senocak.clinked.dto.article.ArticleWrapperDto;
import com.github.senocak.clinked.dto.article.ArticlesDto;
import com.github.senocak.clinked.dto.article.CreateArticleDto;
import com.github.senocak.clinked.dto.article.UpdateArticleDto;
import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.exception.ServerException;
import com.github.senocak.clinked.service.ArticleService;
import com.github.senocak.clinked.service.DtoConverter;
import com.github.senocak.clinked.service.UserService;
import com.github.senocak.clinked.util.AppConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(ArticleController.URL)
public class ArticleController {
    public static final String URL = "/api/v1/article";
    private final UserService userService;
    private final ArticleService articleService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ArticleWrapperDto> create(@RequestBody @Valid CreateArticleDto createArticleDto) throws ServerException {
        Article article = null;
        try{
            article = findArticleBySlugOrId(AppConstants.toSlug(createArticleDto.title()));
        }catch (ServerException serverException){
            log.debug("Caught ServerException, Article not exist in db");
        }
        if (Objects.nonNull(article)){
            log.error("Article exist.");
            throw new ServerException(new String[]{"Article already exist"}, HttpStatus.CONFLICT);
        }

        Article article1 = new Article();
        article1.setId(UUID.randomUUID().toString());
        article1.setTitle(createArticleDto.title());
        article1.setSlug(AppConstants.toSlug(createArticleDto.title()));
        article1.setAuthor(createArticleDto.author());
        article1.setContent(createArticleDto.content());
        article1.setUser(getUserFromContext());
        article1.setCreatedAt(new Date());
        article1.setPublish(createArticleDto.publish());
        Article savedArticle = articleService.persist(article1);

        ArticleWrapperDto wrapperDto = new ArticleWrapperDto(DtoConverter.convertEntityToDto(savedArticle));
        return ResponseEntity.status(HttpStatus.CREATED).body(wrapperDto);
    }

    @GetMapping
    public ResponseEntity<ArticlesDto> getAll(
        @RequestParam(value = "next", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) @Min(0) @Max(99) final int nextPage,
        @RequestParam(value = "max", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) @Min(1) @Max(99) final int maxNumber,
        @RequestParam(value = "username", required = false) final String username
    ) {
        User user = null;
        if (Objects.nonNull(username) && StringUtils.hasText(username))
            user = findUser(username);

        Page<Article> articles = articleService.getAllByUser(user, nextPage, maxNumber);
        List<ArticleDto> dtos = articles.getContent()
                .stream()
                .map(DtoConverter::convertEntityToDto)
                .collect(Collectors.toList());
        ArticlesDto articlesDto = new ArticlesDto(dtos, articles.getTotalElements(), articles.hasNext() ? nextPage + 1 : 0);
        return ResponseEntity.ok(articlesDto);
    }

    @GetMapping(value = "/{slug}")
    public ResponseEntity<ArticleWrapperDto> getSingle(@PathVariable String slug) throws ServerException {
        ArticleDto articleDto = DtoConverter.convertEntityToDto(findArticleBySlugOrId(slug));
        ArticleWrapperDto articleWrapperDto = new ArticleWrapperDto(articleDto);
        return ResponseEntity.ok(articleWrapperDto);
    }

    @PatchMapping("/{slug}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ArticleWrapperDto> update(@PathVariable String slug, @RequestBody @Valid UpdateArticleDto updateArticleDto) throws ServerException {
        Article article = findArticleBySlugOrId(slug);
        checkArticleBelongToUser(article);
        if (Objects.nonNull(updateArticleDto.title())) article.setTitle(updateArticleDto.title());
        if (Objects.nonNull(updateArticleDto.content())) article.setContent(updateArticleDto.content());
        if (Objects.nonNull(updateArticleDto.author())) article.setAuthor(updateArticleDto.author());

        Article savedArticle = articleService.persist(article);
        ArticleWrapperDto wrapperDto = new ArticleWrapperDto(DtoConverter.convertEntityToDto(savedArticle));
        return ResponseEntity.ok(wrapperDto);
    }

    @DeleteMapping(value = "/{slug}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String slug) throws ServerException {
        Article article = findArticleBySlugOrId(slug);
        checkArticleBelongToUser(article);
        articleService.delete(article);
        return ResponseEntity.noContent().build();
    }

    private void checkArticleBelongToUser(Article article) throws ServerException {
        User user = getUserFromContext();
        if (user.getRoles().stream().noneMatch(role -> role.getName() == AppConstants.RoleName.ROLE_ADMIN)
                && !article.getUser().getEmail().equals(user.getEmail())){
            log.error("Article user:{} does not match with jwt user: {}", article.getUser().getEmail(), user.getEmail());
            throw new ServerException(new String[]{"This user does not have the right permission for this operation"},
                    HttpStatus.UNAUTHORIZED);
        }
    }

    private User getUserFromContext() throws ServerException {
        return userService.loggedInUser();
    }

    private Article findArticleBySlugOrId(String slugOrId) throws ServerException {
        Article article = articleService.findArticleBySlugOrId(slugOrId);
        if (Objects.isNull(article)) {
            log.error("Article is not found.");
            throw new ServerException(new String[]{"Article: " + slugOrId}, HttpStatus.NOT_FOUND);
        }
        return article;
    }

    private User findUser(String username) {
        return userService.findByUsername(username);
    }
}