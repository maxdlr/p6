package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final CommentMapper commentMapper;

  public CommentController(
      ArticleRepository articleRepository,
      UserRepository userRepository,
      CommentMapper commentMapper) {
    this.articleRepository = articleRepository;
    this.userRepository = userRepository;
    this.commentMapper = commentMapper;
  }

  @PostMapping
  public ResponseEntity<MessageResponse> addComment(@RequestBody CommentDto commentDto) {

    if (!userRepository.existsById(commentDto.getAuthorId())) {
      return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
    }

    Optional<Article> article = articleRepository.findById(commentDto.getArticleId());

    if (article.isPresent()) {
      Comment comment = commentMapper.toEntity(commentDto);
      article.get().addComment(comment);
      articleRepository.save(article.get());
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.badRequest().body(new MessageResponse("Article not found"));
  }
}
