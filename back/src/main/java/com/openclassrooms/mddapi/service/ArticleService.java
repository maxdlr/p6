package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.exception.ValidationFailureException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
  private final ArticleMapper articleMapper;
  private final EntityManager entityManager;
  private final UserRepository userRepository;
  private final ThemeRepository themeRepository;
  private final ArticleRepository articleRepository;
  private final CommentMapper commentMapper;
  private final CommentRepository commentRepository;

  public ArticleService(
      ArticleMapper articleMapper,
      EntityManager entityManager,
      UserRepository userRepository,
      ThemeRepository themeRepository,
      ArticleRepository articleRepository,
      CommentMapper commentMapper,
      CommentRepository commentRepository) {
    this.articleMapper = articleMapper;
    this.entityManager = entityManager;
    this.userRepository = userRepository;
    this.themeRepository = themeRepository;
    this.articleRepository = articleRepository;
    this.commentMapper = commentMapper;
    this.commentRepository = commentRepository;
  }

  private List<Article> executeFindArticlesByUserQuery(User user) {
    String nativeSql =
        """
                            SELECT a.*
                            FROM ARTICLES a
                            JOIN THEMES t ON a.theme_id = t.id
                            JOIN SUBSCRIPTIONS s ON s.theme_id = t.id
                            WHERE s.user_id = :userId
                        """;

    Query query = entityManager.createNativeQuery(nativeSql, Article.class);
    query.setParameter("userId", user.getId());

    return query.getResultList();
  }

  public List<Article> findArticlesOfUser(User user) {
    return executeFindArticlesByUserQuery(user);
  }

  public List<Article> findArticlesOfUser(Long id) {
    Optional<User> user = userRepository.findById(id);

    if (user.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find user");
    }

    return executeFindArticlesByUserQuery(user.get());
  }

  public List<Article> findArticlesOfUser(String id) {
    try {
      Optional<User> user = userRepository.findById(Long.valueOf(id));

      if (user.isEmpty()) {
        throw new ApiResourceNotFoundException("Cannot find user");
      }

      return executeFindArticlesByUserQuery(user.get());
    } catch (ValidationFailureException | NumberFormatException e) {
      throw new ApiBadPostRequestException(e.getMessage());
    }
  }

  public void save(ArticleDto articleDto) {
    try {
      if (!userRepository.existsById(articleDto.getAuthor().getId())) {
        throw new ApiResourceNotFoundException("Cannot find author of article");
      }

      if (!themeRepository.existsById(articleDto.getTheme().getId())) {
        throw new ApiResourceNotFoundException("Cannot find associated theme of article");
      }

      Article article = articleMapper.toEntity(articleDto);
      articleRepository.save(article);
    } catch (ValidationFailureException | NumberFormatException | NullPointerException e) {
      throw new ApiBadPostRequestException(e.getMessage());
    }
  }

  public Article findArticle(String id) {
    try {
      Optional<Article> article = articleRepository.findById(Long.valueOf(id));

      if (article.isEmpty()) {
        throw new ApiResourceNotFoundException("Cannot find article");
      }

      return article.get();
    } catch (ValidationFailureException | NumberFormatException e) {
      throw new ApiBadPostRequestException(e.getMessage());
    }
  }

  public Article findArticle(Long id) {
    Optional<Article> article = articleRepository.findById(id);

    if (article.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find article");
    }

    return article.get();
  }

  public void addCommentToArticle(CommentDto commentDto) {
    Optional<User> user = userRepository.findById(commentDto.getAuthorId());

    if (user.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find user");
    }

    Optional<Article> article = articleRepository.findById(commentDto.getArticleId());

    if (article.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find article");
    }

    Comment comment = commentMapper.toEntity(commentDto);
    commentRepository.save(comment);
  }
}
