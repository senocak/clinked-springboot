package com.github.senocak.clinked.service;

import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.entity.User;
import com.github.senocak.clinked.repository.ArticleRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public Page<Article> getAllByUser(User user, int nextPage, int maxNumber) {
        Pageable paging = PageRequest.of(nextPage, maxNumber);
        Specification<Article> specification = (root, query, criteriaBuilder) -> {
            Predicate userPredicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            if (user != null)
                userPredicate = criteriaBuilder.equal(root.get("user"), user);

            Predicate categoryPredicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);

            return criteriaBuilder.and(userPredicate, categoryPredicate);
        };
        return articleRepository.findAll(specification, paging);
    }

    public Article findArticleBySlugOrId(String slug){
        return articleRepository.findArticleBySlugOrId(slug).orElse(null);
    }

    public Article persist(Article article) {
        return articleRepository.save(article);
    }

    public void delete(Article article) {
        articleRepository.delete(article);
    }

    public List<Article> findArticlesByCreatedAtIsBetween(Date sevenDaysBeforeString) {
        return articleRepository.findAllWithDateAfter(sevenDaysBeforeString);
    }
}
