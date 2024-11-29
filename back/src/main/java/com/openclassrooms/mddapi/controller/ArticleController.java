package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ArticleValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
  private final ArticleMapper articleMapper;
  private final ArticleValidator articleValidator;
  private final ArticleRepository articleRepository;
  private final ArticleService articleService;
  private final UserRepository userRepository;

  public ArticleController(
      ArticleMapper articleMapper,
      ArticleValidator articleValidator,
      ArticleRepository articleRepository,
      ArticleService articleService,
      UserRepository userRepository) {
    this.articleMapper = articleMapper;
    this.articleValidator = articleValidator;
    this.articleRepository = articleRepository;
    this.articleService = articleService;
    this.userRepository = userRepository;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> createArticle(@RequestBody ArticleDto articleDto) {
    List<String> errors = articleValidator.validateDto(articleDto).getErrors();
    if (!errors.isEmpty()) {
      return ResponseEntity.badRequest().body(new MessageResponse(errors.toString()));
    }

    articleRepository.save(articleMapper.toEntity(articleDto));
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ArticleDto> getArticleById(@PathVariable Long id) {
    try {
      Optional<Article> article = articleRepository.findById(id);
      return article
          .map(value -> ResponseEntity.ok(articleMapper.toDto(value)))
          .orElseGet(() -> ResponseEntity.notFound().build());
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<List<ArticleDto>> getAllArticlesByUserId(@PathVariable Long id) {
    Optional<User> user = userRepository.findById(id);

    return user.map(value -> ResponseEntity.ok(articleService.getArticlesOfUser(value)))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }
}
