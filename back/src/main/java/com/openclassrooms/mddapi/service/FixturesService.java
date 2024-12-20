package com.openclassrooms.mddapi.service;

import com.github.javafaker.Faker;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repository.*;
import java.time.LocalDateTime;
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
  private final CommentRepository commentRepository;

  public FixturesService(
      ThemeRepository themeRepository,
      ArticleRepository articleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder,
      SubscriptionRepository subscriptionRepository,
      CommentRepository commentRepository) {
    this.themeRepository = themeRepository;
    this.articleRepository = articleRepository;
    this.userRepository = userRepository;

    this.faker = new Faker();
    this.passwordEncoder = passwordEncoder;
    this.subscriptionRepository = subscriptionRepository;
    this.commentRepository = commentRepository;
  }

  public List<Theme> createThemes(int number) {

    List<Theme> themes = new ArrayList<>();

    for (int i = 0; i < number; i++) {
      Theme theme = new Theme();
      theme
          .setName(this.faker.food().spice())
          .setDescription(this.faker.chuckNorris().fact() + " " + this.faker.lorem().paragraph())
          .setCreatedAt(
              LocalDateTime.of(
                  this.faker.number().numberBetween(2000, 2024),
                  this.faker.number().numberBetween(1, 12),
                  this.faker.number().numberBetween(1, 31),
                  this.faker.number().numberBetween(1, 23),
                  this.faker.number().numberBetween(1, 59)));
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
          .setContent(this.faker.lorem().paragraph(50))
          .setCreatedAt(
              LocalDateTime.of(
                  this.faker.number().numberBetween(2000, 2024),
                  this.faker.number().numberBetween(1, 12),
                  this.faker.number().numberBetween(1, 31),
                  this.faker.number().numberBetween(1, 23),
                  this.faker.number().numberBetween(1, 59)));
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
          .setPassword(password)
          .setCreatedAt(
              LocalDateTime.of(
                  this.faker.number().numberBetween(2000, 2024),
                  this.faker.number().numberBetween(1, 12),
                  this.faker.number().numberBetween(1, 31),
                  this.faker.number().numberBetween(1, 23),
                  this.faker.number().numberBetween(1, 59)));
      users.add(user);
    }

    if (!userRepository.existsByEmail("test@test.com")) {
      User dummy = new User();
      dummy
          .setEmail("test@test.com")
          .setUsername("test")
          .setPassword(password)
          .setCreatedAt(
              LocalDateTime.of(
                  this.faker.number().numberBetween(2000, 2024),
                  this.faker.number().numberBetween(1, 12),
                  this.faker.number().numberBetween(1, 31),
                  this.faker.number().numberBetween(1, 23),
                  this.faker.number().numberBetween(1, 59)));
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
          .setTheme(themes.get(new Random().nextInt(themes.size())))
          .setCreatedAt(
              LocalDateTime.of(
                  this.faker.number().numberBetween(2000, 2024),
                  this.faker.number().numberBetween(1, 12),
                  this.faker.number().numberBetween(1, 31),
                  this.faker.number().numberBetween(1, 23),
                  this.faker.number().numberBetween(1, 59)));

      if (!subscriptionRepository.existsByUserIdAndThemeId(
          subscription.getUser().getId(), subscription.getTheme().getId())) {
        subscriptionRepository.save(subscription);
      }
    }
  }

  public void createComments(Integer number, List<User> authors, List<Article> articles) {
    for (int i = 0; i < number; i++) {
      Comment comment = new Comment();
      comment
          .setArticle(articles.get(new Random().nextInt(articles.size())))
          .setAuthor(authors.get(new Random().nextInt(authors.size())))
          .setContent(this.faker.chuckNorris().fact())
          .setCreatedAt(
              LocalDateTime.of(
                  this.faker.number().numberBetween(2000, 2024),
                  this.faker.number().numberBetween(1, 12),
                  this.faker.number().numberBetween(1, 31),
                  this.faker.number().numberBetween(1, 23),
                  this.faker.number().numberBetween(1, 59)));
      commentRepository.save(comment);
    }
  }
}
