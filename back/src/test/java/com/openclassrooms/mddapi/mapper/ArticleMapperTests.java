package com.openclassrooms.mddapi.mapper;

import static com.openclassrooms.mddapi.TestUtils.makeArticle;
import static com.openclassrooms.mddapi.TestUtils.makeArticleDto;
import static com.openclassrooms.mddapi.TestUtils.makeTheme;
import static com.openclassrooms.mddapi.TestUtils.makeUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ArticleMapperTests {

  @Mock private UserRepository userRepository;
  @Mock private ThemeRepository themeRepository;

  private ArticleMapper articleMapper;

  @BeforeEach
  void setUp() {
    articleMapper = new ArticleMapperImpl();
    articleMapper.userRepository = userRepository;
    articleMapper.themeRepository = themeRepository;
  }

  @Test
  void testToEntity() {
    User user = makeUser(1, false);
    Theme theme = makeTheme(1);
    ArticleDto articleDto = makeArticleDto(1, theme, user);

    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    when(themeRepository.findById(any(Long.class))).thenReturn(Optional.of(theme));

    Article article = articleMapper.toEntity(articleDto);

    assertNotNull(article);
    assertEquals(articleDto.getId(), article.getId());
    assertEquals(articleDto.getTitle(), article.getTitle());
    assertEquals(articleDto.getContent(), article.getContent());
    assertEquals(user.getId(), article.getAuthor().getId());
    assertEquals(theme.getId(), article.getTheme().getId());
  }

  @Test
  void testToDto() {
    User user = makeUser(1, false);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);

    ArticleDto articleDto = articleMapper.toDto(article);

    assertNotNull(articleDto);
    assertEquals(article.getId(), articleDto.getId());
    assertEquals(article.getTitle(), articleDto.getTitle());
    assertEquals(article.getContent(), articleDto.getContent());
    assertEquals(user.getId(), articleDto.getAuthorId());
    assertEquals(theme.getId(), articleDto.getThemeId());
  }

  @Test
  void testToEntityList() {
    List<ArticleDto> dtoList = new ArrayList<>();
    User user = makeUser(22, false);
    Theme theme = makeTheme(23);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));

    for (int i = 1; i <= 10; i++) {
      ArticleDto articleDto = makeArticleDto(i, theme, user);
      dtoList.add(articleDto);
    }

    List<Article> entityList = articleMapper.toEntity(dtoList);

    assertNotNull(entityList);
    assertEquals(10, entityList.size());
    assertEquals(dtoList.getFirst().getId(), entityList.getFirst().getId());
    assertEquals(dtoList.getLast().getId(), entityList.getLast().getId());
    assertEquals(dtoList.size(), entityList.size());
  }

  @Test
  void testToDtoList() {
    List<Article> entityList = new ArrayList<>();
    User user = makeUser(1, false);
    Theme theme = makeTheme(1);

    for (int i = 0; i < 10; i++) {
      Article article = makeArticle(i, theme, user);
      entityList.add(article);
    }

    List<ArticleDto> dtoList = articleMapper.toDto(entityList);

    assertNotNull(dtoList);
    assertEquals(10, dtoList.size());
    assertEquals(entityList.getFirst().getId(), dtoList.getFirst().getId());
    assertEquals(entityList.getLast().getId(), dtoList.getLast().getId());
    assertEquals(dtoList.size(), entityList.size());
  }
}
