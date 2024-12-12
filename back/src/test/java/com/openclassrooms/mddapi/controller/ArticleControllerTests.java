package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ArticleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static com.openclassrooms.mddapi.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class ArticleControllerTests {
  MockMvc mvc;

  @Mock ThemeRepository themeRepository;
  @Mock UserRepository userRepository;
  @Mock ArticleMapper articleMapper;
  @Mock ArticleRepository articleRepository;
  @Mock ArticleService articleService;

  @BeforeEach
  public void setup() {
    ArticleValidator articleValidator = new ArticleValidator(userRepository, themeRepository);
    ArticleController articleController =
        new ArticleController(
            articleMapper, articleValidator, articleRepository, articleService, userRepository);
    mvc = MockMvcBuilders.standaloneSetup(articleController).build();
  }

  @Test
  public void testAddArticle() throws Exception {
    User user = makeUser(1, false);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);
    ArticleDto articleDto = new ArticleDto();
    articleDto.setTitle("title1").setThemeId(1L).setContent("content1").setAuthorId(1L);

    when(userRepository.existsById(1L)).thenReturn(true);
    when(themeRepository.existsById(1L)).thenReturn(true);
    when(articleMapper.toEntity(articleDto)).thenReturn(article);

    String payload = new ObjectMapper().writeValueAsString(articleDto);

    mvc.perform(
            post("/api/articles")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    articleDto.setTitle("title1").setThemeId(1L).setContent("content1").setAuthorId(2L);
    payload = new ObjectMapper().writeValueAsString(articleDto);

    String userNotfoundResponseBody =
        mvc.perform(
                post("/api/articles")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(userNotfoundResponseBody.contains("User not found"));

    articleDto.setTitle("title1").setThemeId(2L).setContent("content1").setAuthorId(1L);
    payload = new ObjectMapper().writeValueAsString(articleDto);

    String themeNotfoundResponseBody =
        mvc.perform(
                post("/api/articles")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(themeNotfoundResponseBody.contains("Theme not found"));

    payload = new ObjectMapper().writeValueAsString(new ArticleDto());

    mvc.perform(
            post("/api/articles")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetArticleById() throws Exception {
    User user = makeUser(1, false);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);
    ArticleDto articleDto = makeArticleDto(1, theme, user);

    when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
    when(articleMapper.toDto(article)).thenReturn(articleDto);

    mvc.perform(
            get("/api/articles/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.content").value(article.getContent()));

    mvc.perform(
            get("/api/articles/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    mvc.perform(
            get("/api/articles/{id}", "bad-request")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testGetAllArticlesOfUserSubscriptions() throws Exception {
    User author = makeUser(2, false);

    List<Theme> userSubscriptionThemes = new ArrayList<>();
    List<Article> articles = new ArrayList<>();
    List<ArticleDto> articleDtoList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      Theme theme = makeTheme(i);

      for (int k = 0; k < 10; k++) {
        Article article = makeArticle(k, theme, author);
        ArticleDto articleDto = makeArticleDto(k, theme, author);
        articles.add(article);
        articleDtoList.add(articleDto);
      }

      userSubscriptionThemes.add(theme);
    }

    List<Subscription> userSubscriptions = new ArrayList<>();
    User user = makeUser(1, false);

    for (int i = 0; i < userSubscriptionThemes.size() - 1; i++) {
      Subscription subscription = makeSubscription(i, userSubscriptionThemes.get(i), user);
      userSubscriptions.add(subscription);
    }

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(articleService.getArticlesOfUser(user)).thenReturn(articleDtoList);

    mvc.perform(
            get("/api/articles/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(articles.getFirst().getId()))
        .andExpect(
            jsonPath("$[" + (articles.size() - 1) + "].id").value(articles.getLast().getId()));

    mvc.perform(
            get("/api/articles/user/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    User unsubscribedUser = makeUser(3, false);
    when(userRepository.findById(3L)).thenReturn(Optional.of(unsubscribedUser));

    String emptyListResponseBody =
        mvc.perform(
                get("/api/articles/user/{id}", 3L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(emptyListResponseBody.contains("[]"));
  }
}
