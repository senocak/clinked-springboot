package com.github.senocak.clinked.repository;

import com.github.senocak.clinked.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ArticleRepository extends PagingAndSortingRepository<Article, String>, JpaSpecificationExecutor<Article>, JpaRepository<Article, String> {
    @Query(value = "SELECT p FROM Article p WHERE p.id = :slug or p.slug = :slug ")
    Optional<Article> findArticleBySlugOrId(@Param("slug") String slug);
}
