package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleMapper articleMapper;
    private final ArticleService articleService;

    public ArticleController(
            ArticleMapper articleMapper,
            ArticleService articleService
    ) {
        this.articleMapper = articleMapper;
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createArticle(@Valid @RequestBody ArticleDto articleDto) {
        articleService.save(articleDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> getArticleById(@PathVariable String id) {
        Article article = articleService.findArticle(id);
        return ResponseEntity.ok(articleMapper.toDto(article));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ArticleDto>> getAllArticlesByUserId(@PathVariable String id) {
        List<Article> articles = articleService.findArticlesOfUser(id);
        return ResponseEntity.ok(articleMapper.toDto(articles));
    }
}
