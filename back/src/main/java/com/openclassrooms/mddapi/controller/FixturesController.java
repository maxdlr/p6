package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.*;
import com.openclassrooms.mddapi.service.FixturesService;
import java.util.List;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/fixtures")
public class FixturesController {
  private final FixturesService fixturesService;
  private final UserRepository userRepository;
  private final ThemeRepository themeRepository;
  private final ArticleRepository articleRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final CommentRepository commentRepository;

  public FixturesController(
      FixturesService fixturesService,
      UserRepository userRepository,
      ThemeRepository themeRepository,
      ArticleRepository articleRepository,
      SubscriptionRepository subscriptionRepository,
      CommentRepository commentRepository) {
    this.fixturesService = fixturesService;
    this.userRepository = userRepository;
    this.themeRepository = themeRepository;
    this.articleRepository = articleRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.commentRepository = commentRepository;
  }

  @GetMapping("/load")
  public ResponseEntity<MessageResponse> load() {
    try {
      int userCount = 10;
      int themeCount = 20;
      int articleCount = 50;
      int subscriptionCount = 100;
      int commentCount = 300;

      List<User> users = fixturesService.createUsers(userCount);
      List<Theme> themes = fixturesService.createThemes(themeCount);
      List<Article> articles =
          fixturesService.createArticles(new Random().nextInt(articleCount), themes, users);
      fixturesService.createSubscriptions(new Random().nextInt(subscriptionCount), users, themes);
      fixturesService.createComments(new Random().nextInt(commentCount), users, articles);

      return ResponseEntity.ok().body(new MessageResponse("Success Fixtures"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/prune")
  public ResponseEntity<MessageResponse> prune() {
    try {
      commentRepository.deleteAll();
      subscriptionRepository.deleteAll();
      articleRepository.deleteAll();
      themeRepository.deleteAll();
      userRepository.deleteAll();
      return ResponseEntity.ok().body(new MessageResponse("All pruned"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }
}
