package com.openclassrooms.mddapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Service
public class TestUtils {

    public static User makeUser(Integer id, Boolean encoded) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setId((long) id)
                .setUsername("username" + id)
                .setPassword(encoded ? passwordEncoder.encode("password" + id) : "password" + id)
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
                .setDescription("description" + id)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());

        return theme;
    }

    public static ThemeDto makeThemeDto(Integer id) {
        ThemeDto themeDto = new ThemeDto();
        themeDto
                .setId((long) id)
                .setName("theme" + id)
                .setDescription("description" + id)
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

    public static Subscription makeSubscription(Integer id, Theme theme, User user) {
        Subscription subscription = new Subscription();
        subscription
                .setId((long) id)
                .setUser(user)
                .setTheme(theme)
                .setCreatedAt(LocalDateTime.now())
                .setUpdatedAt(LocalDateTime.now());
        return subscription;
    }

    public static HttpHeaders createHeadersWithToken(
            int port, User authenticatedUser, TestRestTemplate restTemplate)
            throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + getJwtToken(port, authenticatedUser, restTemplate));
        return headers;
    }

    public static User getAuthenticatedUser(
            PasswordEncoder passwordEncoder, UserRepository userRepository) {
        User authenticatedUser = new User();
        authenticatedUser
                .setUsername("username1")
                .setPassword(passwordEncoder.encode("password1"))
                .setEmail("email@email.com1")
                .setCreatedAt(LocalDateTime.now());

        userRepository.save(authenticatedUser);
        return authenticatedUser;
    }

    private static String getJwtToken(int port, User authenticatedUser, TestRestTemplate restTemplate)
            throws JsonProcessingException {

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
}
