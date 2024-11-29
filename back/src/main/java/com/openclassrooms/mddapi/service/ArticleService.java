package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
  private final ArticleMapper articleMapper;
  private final EntityManager entityManager;

  public ArticleService(ArticleMapper articleMapper, EntityManager entityManager) {
    this.articleMapper = articleMapper;
    this.entityManager = entityManager;
  }

  public List<ArticleDto> getArticlesOfUser(User user) {
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

    List<Article> articles = query.getResultList();
    return articleMapper.toDto(articles);
  }
}
