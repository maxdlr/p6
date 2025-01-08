package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.createHeadersWithToken;
import static com.openclassrooms.mddapi.TestUtils.getAuthenticatedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.mapper.ThemeMapper;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.*;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.io.IOException;
import java.util.Date;
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
  @Autowired private UserMapper userMapper;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private ArticleRepository articleRepository;
  @Autowired private ThemeMapper themeMapper;
  private User authenticatedUser;
  private String baseUrl;

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
    theme.setName("name").setDescription("description").setCreatedAt(new Date());
    themeRepository.save(theme);
    ThemeDto themeDto = themeMapper.toDto(theme);
    ArticleDto articleDto = new ArticleDto();

    articleDto
        .setTitle("title")
        .setTheme(themeDto)
        .setContent("content")
        .setAuthor(userMapper.toDto(authenticatedUser));

    HttpEntity<ArticleDto> httpEntity =
        new HttpEntity<>(articleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<?> response = restTemplate.postForEntity(baseUrl, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    articleDto.setTheme(
        new ThemeDto().setId(321654987L).setName("name").setDescription("description"));
    HttpEntity<ArticleDto> themeNotFoundHttpEntity =
        new HttpEntity<>(articleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<?> themeNotFoundResponse =
        restTemplate.postForEntity(baseUrl, themeNotFoundHttpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(404), themeNotFoundResponse.getStatusCode());

    articleDto.setAuthor(
        new UserDto().setEmail("email@email.com").setUsername("username").setId(96325L));
    HttpEntity<ArticleDto> authorNotFoundHttpEntity =
        new HttpEntity<>(articleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<?> authorNotFoundResponse =
        restTemplate.postForEntity(baseUrl, authorNotFoundHttpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(404), authorNotFoundResponse.getStatusCode());

    ArticleDto unauthorizedArticleDto = new ArticleDto();
    articleDto.setAuthor(userMapper.toDto(authenticatedUser));
    articleDto.setTheme(themeDto);
    HttpEntity<ArticleDto> unauthorizedHttpEntity =
        new HttpEntity<>(
            unauthorizedArticleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));
    ResponseEntity<?> unauthorizedResponse =
        restTemplate.postForEntity(baseUrl, unauthorizedHttpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(401), unauthorizedResponse.getStatusCode());
  }

  @Test
  public void testGetArticleById() throws JsonProcessingException {
    HttpEntity<?> httpEntity =
        new HttpEntity<>(createHeadersWithToken(port, authenticatedUser, restTemplate));

    Theme theme = new Theme();
    theme.setName("name").setDescription("description").setCreatedAt(new Date());
    themeRepository.save(theme);

    Article article = new Article();
    article
        .setTheme(theme)
        .setTitle("title")
        .setContent("content")
        .setAuthor(authenticatedUser)
        .setCreatedAt(new Date());
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
    HttpEntity<List<ArticleDto>> httpEntity =
        new HttpEntity<>(createHeadersWithToken(port, authenticatedUser, restTemplate));

    Theme theme = new Theme();
    theme.setName("name").setDescription("description").setCreatedAt(new Date());
    themeRepository.save(theme);

    Subscription subscription = new Subscription();
    subscription.setUser(authenticatedUser);
    subscription.setTheme(theme);
    subscription.setCreatedAt(new Date());
    subscriptionRepository.save(subscription);

    for (int i = 0; i < 10; i++) {
      Article article = new Article();
      article
          .setTheme(theme)
          .setTitle("title")
          .setContent("content")
          .setAuthor(authenticatedUser)
          .setCreatedAt(new Date());
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

    assertEquals(HttpStatusCode.valueOf(401), notFoundResponse.getStatusCode());

    ResponseEntity<String> badRequestResponse =
        restTemplate.exchange(
            baseUrl + "/user/bad-request", HttpMethod.GET, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(400), badRequestResponse.getStatusCode());
  }

  @Test
  public void testUpdateArticle() throws IOException {
    Theme theme = new Theme();
    theme.setName("name").setDescription("description").setCreatedAt(new Date());
    themeRepository.save(theme);
    ThemeDto themeDto = themeMapper.toDto(theme);
    ArticleDto editedArticleDto = new ArticleDto();
    Article article = new Article();
    article.setTitle("title").setTheme(theme).setContent("content").setAuthor(authenticatedUser);
    articleRepository.save(article);

    editedArticleDto
        .setTitle("title")
        .setTheme(themeDto)
        .setContent("edited content")
        .setAuthor(userMapper.toDto(authenticatedUser));

    HttpEntity<ArticleDto> httpEntity =
        new HttpEntity<>(
            editedArticleDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<ArticleDto> response =
        restTemplate.exchange(
            baseUrl + "/" + article.getId(), HttpMethod.PUT, httpEntity, ArticleDto.class);

    assert response.getBody() != null;

    ArticleDto responseBody = response.getBody();

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assertEquals(editedArticleDto.getContent(), responseBody.getContent());
  }
}
