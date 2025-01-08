package com.openclassrooms.mddapi.service;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {

  @Mock private EntityManager entityManager;
  @Mock private Query query;
  @Mock private ArticleMapper articleMapper;
  @Mock private ArticleRepository articleRepository;
  @Mock private UserRepository userRepository;
  @Mock private ThemeRepository themeRepository;

  @Mock private CommentRepository commentRepository;

  @Mock private CommentMapper commentMapper;
  private ArticleService articleService;

  @BeforeEach
  void setup() {
    articleService =
        new ArticleService(
            articleMapper,
            entityManager,
            userRepository,
            themeRepository,
            articleRepository,
            commentMapper,
            commentRepository);
  }

  @Test
  void getAllArticlesOfUser() {
    User user = makeUser(1, false);
    UserDto userDto = makeUserDto(1);
    Theme theme = makeTheme(1);
    ThemeDto themeDto = makeThemeDto(1);

    List<Article> articleList = new ArrayList<>();
    List<ArticleDto> articleDtoList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      Article article = makeArticle(i, theme, user);
      ArticleDto articleDto = makeArticleDto(i, themeDto, userDto);
      articleList.add(article);
      articleDtoList.add(articleDto);
    }
    when(entityManager.createNativeQuery(anyString(), eq(Article.class))).thenReturn(query);
    when(query.setParameter(eq("userId"), eq(user.getId()))).thenReturn(query);
    when(query.getResultList()).thenReturn(articleList);

    List<Article> articleCollectionByUser = articleService.findArticlesOfUser(user);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    List<Article> articleCollectionByUserId = articleService.findArticlesOfUser(user.getId());

    assertFalse(articleDtoList.isEmpty());
    assertEquals(articleDtoList.size(), articleCollectionByUser.size());
    assertEquals(articleDtoList.size(), articleCollectionByUserId.size());

    assertThrows(UsernameNotFoundException.class, () -> articleService.findArticlesOfUser(654L));

    assertThrows(
        ApiBadPostRequestException.class, () -> articleService.findArticlesOfUser("bad-request"));
  }

  @Test
  public void testFindArticleByString() {
    Theme theme = makeTheme(1);
    User author = makeUser(987, true);
    Article article = makeArticle(1, theme, author);

    when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

    Article articleByString = articleService.findArticle(article.getId().toString());
    Article articleByLong = articleService.findArticle(article.getId().toString());

    assertEquals(articleByString.getId(), article.getId());
    assertEquals(articleByLong.getId(), article.getId());

    assertThrows(ApiResourceNotFoundException.class, () -> articleService.findArticle("7987"));

    assertThrows(ApiBadPostRequestException.class, () -> articleService.findArticle("bad-request"));
  }

  @Test
  public void testFindArticleByLong() {
    Theme theme = makeTheme(1);
    User author = makeUser(987, true);
    Article article = makeArticle(1, theme, author);

    when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

    Article articleByString = articleService.findArticle(article.getId());
    Article articleByLong = articleService.findArticle(article.getId());

    assertEquals(articleByString.getId(), article.getId());
    assertEquals(articleByLong.getId(), article.getId());

    assertThrows(ApiResourceNotFoundException.class, () -> articleService.findArticle(7987L));
    assertThrows(ApiBadPostRequestException.class, () -> articleService.findArticle("bad-request"));
  }

  @Test
  public void testCreateArticle() {
    User user = makeUser(1, true);
    UserDto userDto = makeUserDto(1);
    Theme theme = makeTheme(1);
    ThemeDto themeDto = makeThemeDto(1);

    when(userRepository.existsById(user.getId())).thenReturn(true);
    when(themeRepository.existsById(theme.getId())).thenReturn(true);

    ArticleDto articleDto = new ArticleDto();
    articleDto.setAuthor(userDto);
    articleDto.setTheme(themeDto);

    Article article = new Article();
    article.setAuthor(user);
    article.setTheme(theme);

    when(articleMapper.toEntity(articleDto)).thenReturn(article);
    articleService.save(articleDto);

    verify(articleRepository).save(any(Article.class));

    assertThrows(ApiBadPostRequestException.class, () -> articleService.save(new ArticleDto()));
  }

  @Test
  public void testAddCommentToArticle() {
    User user = makeUser(1, true);
    UserDto userDto = makeUserDto(1);
    Theme theme = makeTheme(1);
    Article article = new Article();
    article.setId(1L).setAuthor(user).setTheme(theme).setContent("content").setTitle("title");

    Comment comment = new Comment();
    comment.setArticle(article).setAuthor(user).setContent("comment");

    CommentDto commentDto = new CommentDto();
    commentDto.setArticleId(article.getId()).setAuthor(userDto).setContent("comment");

    when(commentMapper.toEntity(commentDto)).thenReturn(comment);
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

    articleService.addCommentToArticle(commentDto);

    verify(commentRepository).save(any(Comment.class));

    commentDto.setAuthor(new UserDto());
    assertThrows(
        UsernameNotFoundException.class, () -> articleService.addCommentToArticle(commentDto));

    commentDto.setAuthor(new UserDto());
    commentDto.setArticleId(anyLong());
    assertThrows(
        UsernameNotFoundException.class, () -> articleService.addCommentToArticle(commentDto));
  }
}
