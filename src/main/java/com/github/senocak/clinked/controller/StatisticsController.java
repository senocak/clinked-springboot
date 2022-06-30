package com.github.senocak.clinked.controller;

import com.github.senocak.clinked.entity.Article;
import com.github.senocak.clinked.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(StatisticsController.URL)
public class StatisticsController {
    public static final String URL = "/api/v1/statistics";
    private final ArticleService articleService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Integer>> statistics() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date sevenDaysBefore = calendar.getTime();

        Map<Object, List<Article>> objectListMap =
                articleService.findArticlesByCreatedAtIsBetween(sevenDaysBefore).stream()
                        .collect(Collectors.groupingBy(article -> {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(article.getCreatedAt());
                            return cal.get(Calendar.DAY_OF_MONTH)+"/"+ cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR);
                        }));

        Map<String, Integer> map = new HashMap<>();
        for (Map.Entry<Object, List<Article>> entry: objectListMap.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().size());
        }

        return ResponseEntity.ok(map);
    }
}
