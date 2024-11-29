package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

  private final ArticleMapper articleMapper;
  private final SubscriptionRepository subscriptionRepository;

  public ArticleService(
      ArticleMapper articleMapper, SubscriptionRepository subscriptionRepository) {
    this.articleMapper = articleMapper;
    this.subscriptionRepository = subscriptionRepository;
  }

  public List<ArticleDto> getArticlesOfUser(User user) {
    List<Article> articles = new ArrayList<>();
    List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);

    for (int i = 0; i < subscriptions.size() - 1; i++) {
      Theme theme = subscriptions.get(i).getTheme();
      articles = theme.getArticleList();
    }

    return articleMapper.toDto(articles);
  }
}
