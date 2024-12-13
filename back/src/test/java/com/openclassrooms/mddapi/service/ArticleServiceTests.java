package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.mddapi.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {

  @Mock private EntityManager entityManager;
  @Mock private Query query;
  @Mock private ArticleMapper articleMapper;
  private ArticleService articleService;

  @BeforeEach
  void setup() {
    articleService = new ArticleService(articleMapper, entityManager);
  }

  @Test
  void getAllArticlesOfUser() {
    User user = makeUser(1, false);
    Theme theme = makeTheme(1);
    List<Article> articleList = new ArrayList<>();
    List<ArticleDto> articleDtoList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      Article article = makeArticle(i, theme, user);
      ArticleDto articleDto = makeArticleDto(i, theme, user);
      articleList.add(article);
      articleDtoList.add(articleDto);
    }
    when(entityManager.createNativeQuery(anyString(), eq(Article.class))).thenReturn(query);
    when(query.setParameter(eq("userId"), eq(user.getId()))).thenReturn(query);
    when(query.getResultList()).thenReturn(articleList);
    when(articleMapper.toDto(articleList)).thenReturn(articleDtoList);

    List<ArticleDto> servedArticleDtoList = articleService.getArticlesOfUser(user);

    assertFalse(articleDtoList.isEmpty());
    assertEquals(articleDtoList.size(), servedArticleDtoList.size());
    assertEquals(articleDtoList, servedArticleDtoList);
  }
}
