package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.createHeadersWithToken;
import static com.openclassrooms.mddapi.TestUtils.getAuthenticatedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.Date;
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
  @Autowired private UserRepository userRepository;
  @Autowired private ArticleRepository articleRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private CommentRepository commentRepository;
  @Autowired private UserMapper userMapper;

  private String baseUrl;

  private User authenticatedUser;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/comments";
    authenticatedUser = getAuthenticatedUser(passwordEncoder, userRepository);
  }

  @AfterEach
  public void tearDown() {
    commentRepository.deleteAll();
    articleRepository.deleteAll();
    themeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void testAddComment() throws JsonProcessingException {
    Theme theme = new Theme();
    theme.setName("theme168541").setCreatedAt(new Date());
    themeRepository.save(theme);

    Article article = new Article();
    article
        .setAuthor(authenticatedUser)
        .setTitle("title")
        .setContent("content")
        .setCreatedAt(new Date())
        .setTheme(theme);
    articleRepository.save(article);

    CommentDto commentDto = new CommentDto();
    commentDto.setContent("This is a comment");
    commentDto.setAuthor(userMapper.toDto(authenticatedUser));
    commentDto.setArticleId(article.getId());
    commentDto.setCreatedAt(new Date());

    HttpEntity<CommentDto> httpEntity =
        new HttpEntity<>(commentDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> response =
        restTemplate.exchange(baseUrl, HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
  }
}
