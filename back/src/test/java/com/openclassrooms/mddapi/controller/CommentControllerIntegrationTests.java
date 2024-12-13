package com.openclassrooms.mddapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CommentControllerIntegrationTests {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private PasswordEncoder passwordEncoder;

  private String baseUrl;
  private String jwtToken;

  private User authenticatedUser;
  @Autowired private UserRepository userRepository;
  @Autowired private ArticleRepository articleRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private CommentRepository commentRepository;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/comments";
    jwtToken = getJwtToken();
  }

  private String getJwtToken() throws JsonProcessingException {
    authenticatedUser = new User();
    authenticatedUser
        .setUsername("username1")
        .setPassword(passwordEncoder.encode("password1"))
        .setEmail("email@email.com1")
        .setCreatedAt(LocalDateTime.now());

    userRepository.save(authenticatedUser);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(authenticatedUser.getEmail());
    loginRequest.setPassword("password1");

    ResponseEntity<String> loginResponse =
        restTemplate.postForEntity(
            "http://localhost:" + port + "/api/auth/login", loginRequest, String.class);
    assertEquals(HttpStatusCode.valueOf(200), loginResponse.getStatusCode());

    String responseBody = loginResponse.getBody();
    assert responseBody != null;

    return new ObjectMapper().readTree(responseBody).get("token").asText();
  }

  @AfterEach
  public void tearDown() {
    commentRepository.deleteAll();
    articleRepository.deleteAll();
    themeRepository.deleteAll();
    userRepository.deleteAll();
  }

  private HttpHeaders createHeadersWithToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + jwtToken);
    return headers;
  }

  @Test
  public void testAddComment() {
    Theme theme = new Theme();
    theme.setName("theme168541").setCreatedAt(LocalDateTime.now());
    themeRepository.save(theme);

    Article article = new Article();
    article
        .setAuthor(authenticatedUser)
        .setTitle("title")
        .setContent("content")
        .setCreatedAt(LocalDateTime.now())
        .setTheme(theme);
    articleRepository.save(article);

    CommentDto commentDto = new CommentDto();
    commentDto.setContent("This is a comment");
    commentDto.setAuthorId(authenticatedUser.getId());
    commentDto.setArticleId(article.getId());
    commentDto.setCreatedAt(LocalDateTime.now());

    HttpEntity<CommentDto> httpEntity = new HttpEntity<>(commentDto, createHeadersWithToken());

    ResponseEntity<String> response =
        restTemplate.exchange(baseUrl, HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }
}