package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
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

  public FixturesController(
      FixturesService fixturesService,
      UserRepository userRepository,
      ThemeRepository themeRepository,
      ArticleRepository articleRepository,
      SubscriptionRepository subscriptionRepository) {
    this.fixturesService = fixturesService;
    this.userRepository = userRepository;
    this.themeRepository = themeRepository;
    this.articleRepository = articleRepository;
    this.subscriptionRepository = subscriptionRepository;
  }

  @GetMapping("/load")
  public ResponseEntity<MessageResponse> load() {
    try {
      int userCount = 10;
      int themeCount = 5;
      int articleCount = 50;
      int subscriptionCount = 100;

      List<User> users = fixturesService.createUsers(userCount);
      List<Theme> themes = fixturesService.createThemes(themeCount);
      fixturesService.createArticles(new Random().nextInt(articleCount), themes, users);
      fixturesService.createSubscriptions(new Random().nextInt(subscriptionCount), users, themes);

      return ResponseEntity.ok().body(new MessageResponse("Success Fixtures"));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
    }
  }

  @GetMapping("/prune")
  public ResponseEntity<MessageResponse> prune() {
    try {
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
