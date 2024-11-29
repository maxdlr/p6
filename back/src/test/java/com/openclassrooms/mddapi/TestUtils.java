package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;

import java.time.LocalDateTime;

public class TestUtils {
  public static User makeUser(Integer id) {
    User user = new User();
    user.setId((long) id)
        .setUsername("username" + id)
        .setPassword("password" + id)
        .setEmail("email@email.com" + id)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());
    return user;
  }

  public static UserDto makeUserDto(Integer id) {
    UserDto userDto = new UserDto();
    userDto
        .setId((long) id)
        .setUsername("username" + id)
        .setPassword("password" + id)
        .setEmail("email@email.com" + id)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());
    return userDto;
  }

  public static Theme makeTheme(Integer id) {
    Theme theme = new Theme();
    theme
        .setId((long) id)
        .setName("theme" + id)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

    return theme;
  }

  public static ThemeDto makeThemeDto(Integer id) {
    ThemeDto themeDto = new ThemeDto();
    themeDto
        .setId((long) id)
        .setName("theme" + id)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

    return themeDto;
  }

  public static Article makeArticle(Integer id, Theme theme, User user) {
    Article article = new Article();
    article
        .setId((long) id)
        .setTitle("title" + id)
        .setTheme(theme)
        .setContent("content" + id)
        .setAuthor(user)
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());
    return article;
  }

  public static ArticleDto makeArticleDto(Integer id, Theme theme, User user) {
    ArticleDto articleDto = new ArticleDto();
    articleDto
        .setId((long) id)
        .setTitle("title" + id)
        .setThemeId(theme.getId())
        .setContent("content" + id)
        .setAuthorId(user.getId())
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());
    return articleDto;
  }
}
