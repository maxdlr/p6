package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.createHeadersWithToken;
import static com.openclassrooms.mddapi.TestUtils.getAuthenticatedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ArticleControllerIntegrationTests {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private UserRepository userRepository;

  @Autowired private SubscriptionRepository subscriptionRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  private String baseUrl;

  private User authenticatedUser;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private ArticleRepository articleRepository;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/articles";
    authenticatedUser = getAuthenticatedUser(passwordEncoder, userRepository);
  }

  @AfterEach
  public void tearDown() {
    articleRepository.deleteAll();
    subscriptionRepository.deleteAll();
    userRepository.deleteAll();
    themeRepository.deleteAll();
  }

  @Test
  public void testCreateArticle() throws JsonProcessingException {
    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(LocalDateTime.now());
    themeRepository.save(theme);
    ArticleDto articleDto = new ArticleDto();

    articleDto
        .setTitle("title")
        .setThemeId(theme.getId())
        .setContent("content")
        .setAuthorId(authenticatedUser.getId());

    HttpEntity<ArticleDto> httpEntity = new HttpEntity<>(articleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<?> response = restTemplate.postForEntity(baseUrl, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    articleDto.setThemeId(96325L);
    HttpEntity<ArticleDto> themeNotFoundHttpEntity =
        new HttpEntity<>(articleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<?> themeNotFoundResponse =
        restTemplate.postForEntity(baseUrl, themeNotFoundHttpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(404), themeNotFoundResponse.getStatusCode());

    articleDto.setAuthorId(96325L);
    HttpEntity<ArticleDto> authorNotFoundHttpEntity =
        new HttpEntity<>(articleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<?> authorNotFoundResponse =
        restTemplate.postForEntity(baseUrl, authorNotFoundHttpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(404), authorNotFoundResponse.getStatusCode());

    ArticleDto badRequestArticleDto = new ArticleDto();
    articleDto.setAuthorId(authenticatedUser.getId());
    articleDto.setThemeId(theme.getId());
    HttpEntity<ArticleDto> badRequestHttpEntity =
        new HttpEntity<>(badRequestArticleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));
    ResponseEntity<?> badRequestResponse =
        restTemplate.postForEntity(baseUrl, badRequestHttpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(400), badRequestResponse.getStatusCode());
  }

  @Test
  public void testGetArticleById() throws JsonProcessingException {
    HttpEntity<?> httpEntity = new HttpEntity<>(createHeadersWithToken(port, authenticatedUser, restTemplate));

    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(LocalDateTime.now());
    themeRepository.save(theme);

    Article article = new Article();
    article
        .setTheme(theme)
        .setTitle("title")
        .setContent("content")
        .setAuthor(authenticatedUser)
        .setCreatedAt(LocalDateTime.now());
    articleRepository.save(article);

    ResponseEntity<ArticleDto> response =
        restTemplate.exchange(
            baseUrl + "/" + article.getId(), HttpMethod.GET, httpEntity, ArticleDto.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    ResponseEntity<ArticleDto> notFoundRequestResponse =
        restTemplate.exchange(
            baseUrl + "/9865410685241", HttpMethod.GET, httpEntity, ArticleDto.class);

    assertEquals(HttpStatusCode.valueOf(404), notFoundRequestResponse.getStatusCode());

    ResponseEntity<ArticleDto> badRequestResponse =
        restTemplate.exchange(
            baseUrl + "/bad-request", HttpMethod.GET, httpEntity, ArticleDto.class);

    assertEquals(HttpStatusCode.valueOf(400), badRequestResponse.getStatusCode());
  }

  @Test
  public void testFindArticlesOfUser() throws IOException {
    HttpEntity<List<ArticleDto>> httpEntity = new HttpEntity<>(createHeadersWithToken(port, authenticatedUser, restTemplate));

    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(LocalDateTime.now());
    themeRepository.save(theme);

    Subscription subscription = new Subscription();
    subscription.setUser(authenticatedUser);
    subscription.setTheme(theme);
    subscription.setCreatedAt(LocalDateTime.now());
    subscriptionRepository.save(subscription);

    for (int i = 0; i < 10; i++) {
      Article article = new Article();
      article
          .setTheme(theme)
          .setTitle("title")
          .setContent("content")
          .setAuthor(authenticatedUser)
          .setCreatedAt(LocalDateTime.now());
      articleRepository.save(article);
    }

    ResponseEntity<String> response =
        restTemplate.exchange(
            baseUrl + "/user/" + authenticatedUser.getId(),
            HttpMethod.GET,
            httpEntity,
            String.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assertEquals(10, (new ObjectMapper().readTree(response.getBody())).size());

    ResponseEntity<String> notFoundResponse =
        restTemplate.exchange(
            baseUrl + "/user/987987987", HttpMethod.GET, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(404), notFoundResponse.getStatusCode());

    ResponseEntity<String> badRequestResponse =
        restTemplate.exchange(
            baseUrl + "/user/bad-request", HttpMethod.GET, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(400), badRequestResponse.getStatusCode());
  }
}
