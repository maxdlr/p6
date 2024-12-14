package com.openclassrooms.mddapi.mapper;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentMapperTests {

  private CommentMapper commentMapper;

  @Mock private ArticleRepository articleRepository;

  @Mock private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    commentMapper = new CommentMapperImpl();
    commentMapper.articleRepository = articleRepository;
    commentMapper.userRepository = userRepository;
  }

  @Test
  public void testToDto() {
    User user = makeUser(1, true);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);

    Comment comment = new Comment();
    comment
        .setArticle(article)
        .setAuthor(user)
        .setId(1L)
        .setContent("content")
        .setCreatedAt(LocalDateTime.now());

    CommentDto commentDto = commentMapper.toDto(comment);

    assertNotNull(commentDto);
    assertEquals(comment.getId(), commentDto.getId());
  }

  @Test
  public void testToEntity() {
    User user = makeUser(1, true);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);

    CommentDto commentDto = new CommentDto();
    commentDto
        .setArticleId(article.getId())
        .setAuthorId(user.getId())
        .setId(1L)
        .setContent("content")
        .setCreatedAt(LocalDateTime.now());

    Comment comment = commentMapper.toEntity(commentDto);

    assertNotNull(comment);
    assertEquals(commentDto.getId(), comment.getId());
  }

  @Test
  public void testToDtoList() {
    User user = makeUser(1, true);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);

    List<Comment> commentList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      Comment comment = new Comment();
      comment
          .setArticle(article)
          .setAuthor(user)
          .setId((long) i)
          .setContent("content")
          .setCreatedAt(LocalDateTime.now());

      commentList.add(comment);
    }

    List<CommentDto> commentDtoList = commentMapper.toDto(commentList);

    assertNotNull(commentDtoList);
    assertEquals(10, commentDtoList.size());
  }

  @Test
  public void testToEntityList() {
    User user = makeUser(1, true);
    Theme theme = makeTheme(1);
    Article article = makeArticle(1, theme, user);

    List<CommentDto> commentDtoList = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      CommentDto commentDto = new CommentDto();
      commentDto
          .setArticleId(article.getId())
          .setAuthorId(user.getId())
          .setId((long) i)
          .setContent("content")
          .setCreatedAt(LocalDateTime.now());
      commentDtoList.add(commentDto);
    }

    List<Comment> commentList = commentMapper.toEntity(commentDtoList);

    assertNotNull(commentList);
    assertEquals(10, commentList.size());
  }
}