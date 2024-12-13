package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
  private final ArticleService articleService;

  public CommentController(ArticleService articleService) {
    this.articleService = articleService;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> addComment(@RequestBody CommentDto commentDto) {
    articleService.addCommentToArticle(commentDto);
    return ResponseEntity.ok().build();
  }
}
