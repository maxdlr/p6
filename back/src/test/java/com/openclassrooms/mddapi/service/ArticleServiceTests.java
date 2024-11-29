package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import static com.openclassrooms.mddapi.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {

  @Mock ArticleMapper articleMapper;
  @Mock SubscriptionRepository subscriptionRepository;
  private ArticleService articleService;

  @BeforeEach
  void setUp() {
    articleService = new ArticleService(articleMapper, subscriptionRepository);
  }

  @Test
  void getAllArticlesOfUser() {
    User user = makeUser(1);
    Theme theme = makeTheme(1);
    List<Subscription> subscriptionList = new ArrayList<>();
    List<Article> articleList = new ArrayList<>();
    List<ArticleDto> articleDtoList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      Article article = makeArticle(i, theme, user);
      ArticleDto articleDto = makeArticleDto(i, theme, user);
      Subscription subscription = makeSubscription(i, theme, user);
      subscriptionList.add(subscription);
      articleList.add(article);
      articleDtoList.add(articleDto);
    }

    when(subscriptionRepository.findAllByUser(user)).thenReturn(subscriptionList);
    when(articleMapper.toDto(articleList)).thenReturn(articleDtoList);
    //    when(user.getSubscriptionList()).thenReturn(subscriptionList);
    //    when(theme.getArticleList()).thenReturn(articleList);

    List<ArticleDto> servedArticleDtoList = articleService.getArticlesOfUser(user);

    assertFalse(articleDtoList.isEmpty());
  }
}
