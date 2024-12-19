package com.openclassrooms.mddapi.service;

import com.github.javafaker.Faker;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class FixturesService {
  private final ThemeRepository themeRepository;
  private final Faker faker;
  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SubscriptionRepository subscriptionRepository;

  public FixturesService(
      ThemeRepository themeRepository,
      ArticleRepository articleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      SubscriptionRepository subscriptionRepository) {
    this.themeRepository = themeRepository;
    this.articleRepository = articleRepository;
    this.userRepository = userRepository;

    this.faker = new Faker();
    this.passwordEncoder = passwordEncoder;
    this.subscriptionRepository = subscriptionRepository;
  }

  public List<Theme> createThemes(int number) {

    List<Theme> themes = new ArrayList<>();

    for (int i = 0; i < number; i++) {
      Theme theme = new Theme();
      theme
          .setName(this.faker.food().spice())
          .setDescription(this.faker.chuckNorris().fact() + " " + this.faker.lorem().paragraph());
      themes.add(theme);
    }
    themeRepository.saveAll(themes);

    return themes;
  }

  public List<Article> createArticles(int number, List<Theme> themes, List<User> authors) {
    List<Article> articles = new ArrayList<>();

    for (int i = 0; i < number; i++) {
      Article article = new Article();
      article
          .setTheme(themes.get(new Random().nextInt(themes.size())))
          .setAuthor(authors.get(new Random().nextInt(authors.size())))
          .setTitle(this.faker.food().ingredient())
          .setContent(this.faker.lorem().paragraph(50));
      articles.add(article);
    }
    articleRepository.saveAll(articles);
    return articles;
  }

  public List<User> createUsers(int number) {
    List<User> users = new ArrayList<>();
    String password = passwordEncoder.encode("azertyazerty");

    for (int i = 0; i < number; i++) {
      User user = new User();
      user.setEmail(this.faker.internet().emailAddress())
          .setUsername(this.faker.name().username())
          .setPassword(password);
      users.add(user);
    }

    if (!userRepository.existsByEmail("test@test.com")) {
      User dummy = new User();
      dummy.setEmail("test@test.com").setUsername("test").setPassword(password);
      users.add(dummy);
    }

    userRepository.saveAll(users);
    return users;
  }

  public void createSubscriptions(Integer number, List<User> users, List<Theme> themes) {
    for (int i = 0; i < number; i++) {
      Subscription subscription = new Subscription();
      subscription
          .setUser(users.get(new Random().nextInt(users.size())))
          .setTheme(themes.get(new Random().nextInt(themes.size())));
      subscriptionRepository.save(subscription);
    }
  }
}
