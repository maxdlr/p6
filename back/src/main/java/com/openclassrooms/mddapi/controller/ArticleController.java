package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ArticleService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
  private final ArticleMapper articleMapper;
  private final ArticleService articleService;

  public ArticleController(ArticleMapper articleMapper, ArticleService articleService) {
    this.articleMapper = articleMapper;
    this.articleService = articleService;
  }

  @PostMapping
  public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody ArticleDto articleDto) {
    Article article = articleService.save(articleDto);
    ArticleDto articleDto1 = articleMapper.toDto(article);
    return ResponseEntity.ok().body(articleDto1);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ArticleDto> getArticleById(@PathVariable String id) {
    Article article = articleService.findArticle(id);
    return ResponseEntity.ok(articleMapper.toDto(article));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ArticleDto> updateArticle(
      @PathVariable String id, @Valid @RequestBody ArticleDto articleDto) {
    Article article = articleService.update(id, articleDto);
    ArticleDto articleDto1 = articleMapper.toDto(article);
    return ResponseEntity.ok().body(articleDto1);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<ArticleDto>> getAllArticlesByUserId(@PathVariable String id) {
    List<Article> articles = articleService.findArticlesOfUser(id);
    return ResponseEntity.ok(articleMapper.toDto(articles));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<MessageResponse> deleteArticle(@PathVariable String id) {
    articleService.delete(id);
    return ResponseEntity.ok().build();
  }
}
