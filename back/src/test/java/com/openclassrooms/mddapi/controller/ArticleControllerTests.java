package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.ArticleValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

  @BeforeEach
  public void setup() {
    ArticleValidator articleValidator = new ArticleValidator(userRepository, themeRepository);
    ArticleController articleController =
        new ArticleController(articleMapper, articleValidator, articleRepository);
    mvc = MockMvcBuilders.standaloneSetup(articleController).build();
  }

  @Test
  public void testAddArticle() throws Exception {
    User user = makeUser(1);
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
}
