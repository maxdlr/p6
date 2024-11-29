package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.service.ArticleValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
  private final ArticleMapper articleMapper;
  private final ArticleValidator articleValidator;
  private final ArticleRepository articleRepository;

  public ArticleController(
      ArticleMapper articleMapper,
      ArticleValidator articleValidator,
      ArticleRepository articleRepository) {
    this.articleMapper = articleMapper;
    this.articleValidator = articleValidator;
    this.articleRepository = articleRepository;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> createArticle(@RequestBody ArticleDto articleDto) {

    List<String> errors = articleValidator.validateDto(articleDto).getErrors();

    if (!errors.isEmpty()) {
      return ResponseEntity.badRequest().body(new MessageResponse(errors.toString()));
    }

    Article article = articleMapper.toEntity(articleDto);

    articleRepository.save(article);

    return ResponseEntity.ok().build();
  }
}
