package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CommentControllerTests {

  @Mock ArticleRepository articleRepository;
  @Mock UserRepository userRepository;
  @Mock CommentMapper commentMapper;
  private MockMvc mvc;

  @BeforeEach
  public void setup() {
    CommentController commentController =
        new CommentController(articleRepository, userRepository, commentMapper);
    mvc = MockMvcBuilders.standaloneSetup(commentController).build();
  }

  @Test
  public void testAddCommentToArticle() throws Exception {
    Theme theme = makeTheme(1);
    User user = makeUser(1);
    Article article = makeArticle(1, theme, user);

    CommentDto commentDto = new CommentDto();
    commentDto.setAuthorId(user.getId()).setArticleId(article.getId()).setContent("content1");

    Comment comment = new Comment();
    comment
        .setArticle(article)
        .setAuthor(user)
        .setCreatedAt(LocalDateTime.now())
        .setContent("content1");

    when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));
    when(userRepository.existsById(user.getId())).thenReturn(true);
    when(commentMapper.toEntity(commentDto)).thenReturn(comment);

    String payload = new ObjectMapper().writeValueAsString(commentDto);

    mvc.perform(
            post("/api/comments")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    payload = new ObjectMapper().writeValueAsString(new CommentDto());

    mvc.perform(
            post("/api/comments")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    CommentDto userNotfoundCommentDto = new CommentDto();
    userNotfoundCommentDto.setAuthorId(2L).setArticleId(article.getId()).setContent("content1");

    payload = new ObjectMapper().writeValueAsString(userNotfoundCommentDto);

    mvc.perform(
            post("/api/comments")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("User not found"));

    assertTrue(article.getComments().contains(comment));
    assertEquals(comment.getArticle(), article);
  }
}
