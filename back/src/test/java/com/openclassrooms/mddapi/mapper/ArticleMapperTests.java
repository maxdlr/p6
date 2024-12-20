package com.openclassrooms.mddapi.mapper;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
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
  @Mock private CommentRepository commentRepository;
  @Mock private ThemeMapper themeMapper;
  @Mock private UserMapper userMapper;
  @Mock private CommentMapper commentMapper;

  private ArticleMapper articleMapper;

  @BeforeEach
  void setUp() {
    articleMapper = new ArticleMapperImpl();
    articleMapper.userRepository = userRepository;
    articleMapper.themeRepository = themeRepository;
    articleMapper.themeMapper = themeMapper;
    articleMapper.userMapper = userMapper;
    articleMapper.commentMapper = commentMapper;
    articleMapper.commentRepository = commentRepository;
  }

  @Test
  void testToEntity() {
    User user = makeUser(1, false);
    UserDto userDto = makeUserDto(1);
    Theme theme = makeTheme(1);
    ThemeDto themeDto = makeThemeDto(1);
    ArticleDto articleDto = makeArticleDto(1, themeDto, userDto);

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
    UserDto userDto = makeUserDto(1);
    Theme theme = makeTheme(1);
    ThemeDto themeDto = makeThemeDto(1);

    when(themeMapper.toDto(theme)).thenReturn(themeDto);
    when(userMapper.toDto(user)).thenReturn(userDto);

    Article article = makeArticle(1, theme, user);

    when(commentRepository.findAllByArticle(article)).thenReturn(new ArrayList<>());

    ArticleDto articleDto = articleMapper.toDto(article);

    assertNotNull(articleDto);
    assertEquals(article.getId(), articleDto.getId());
    assertEquals(article.getTitle(), articleDto.getTitle());
    assertEquals(article.getContent(), articleDto.getContent());
    assertEquals(user.getId(), articleDto.getAuthor().getId());
    assertEquals(theme.getId(), articleDto.getTheme().getId());
  }

  @Test
  void testToEntityList() {
    List<ArticleDto> dtoList = new ArrayList<>();
    User user = makeUser(22, false);
    UserDto userDto = makeUserDto(22);
    Theme theme = makeTheme(23);
    ThemeDto themeDto = makeThemeDto(23);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));

    for (int i = 1; i <= 10; i++) {
      ArticleDto articleDto = makeArticleDto(i, themeDto, userDto);
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
    List<Article> articleList = new ArrayList<>();
    User user = makeUser(1, false);
    UserDto userDto = makeUserDto(1);
    Theme theme = makeTheme(1);
    ThemeDto themeDto = makeThemeDto(1);

    for (int i = 0; i < 10; i++) {
      Article article = makeArticle(i, theme, user);
      articleList.add(article);
    }

    when(themeMapper.toDto(theme)).thenReturn(themeDto);
    when(userMapper.toDto(user)).thenReturn(userDto);

    List<ArticleDto> dtoList = articleMapper.toDto(articleList);

    assertNotNull(dtoList);
    assertEquals(10, dtoList.size());
    assertEquals(articleList.getFirst().getId(), dtoList.getFirst().getId());
    assertEquals(articleList.getLast().getId(), dtoList.getLast().getId());
    assertEquals(dtoList.size(), articleList.size());
  }
}
