package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.createHeadersWithToken;
import static com.openclassrooms.mddapi.TestUtils.getAuthenticatedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class SubscriptionControllerIntegrationTests {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private UserRepository userRepository;

  @Autowired private SubscriptionRepository subscriptionRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  private String baseUrl;

  private User authenticatedUser;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private ArticleRepository articleRepository;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/subscriptions";
    authenticatedUser = getAuthenticatedUser(passwordEncoder, userRepository);
  }

  @AfterEach
  public void tearDown() {
    articleRepository.deleteAll();
    subscriptionRepository.deleteAll();
    userRepository.deleteAll();
    themeRepository.deleteAll();
  }

  @Test
  public void testSubscribe() throws JsonProcessingException {
    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(new Date());
    themeRepository.save(theme);

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUserId(authenticatedUser.getId());
    subscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> httpEntity =
        new HttpEntity<>(
            subscriptionRequest, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> response =
        restTemplate.exchange(baseUrl + "/subscribe", HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

    SubscriptionRequest userNotFoundSubscriptionRequest = new SubscriptionRequest();
    userNotFoundSubscriptionRequest.setUserId(888L);
    userNotFoundSubscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> userNotFountHttpEntity =
        new HttpEntity<>(
            userNotFoundSubscriptionRequest,
            createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> userNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/subscribe", HttpMethod.POST, userNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), userNotFoundResponse.getStatusCode());

    SubscriptionRequest themeNotFoundSubscriptionRequest = new SubscriptionRequest();
    themeNotFoundSubscriptionRequest.setUserId(authenticatedUser.getId());
    themeNotFoundSubscriptionRequest.setThemeId(888L);

    HttpEntity<SubscriptionRequest> themeNotFountHttpEntity =
        new HttpEntity<>(
            themeNotFoundSubscriptionRequest,
            createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> themeNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/subscribe", HttpMethod.POST, themeNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), themeNotFoundResponse.getStatusCode());

    SubscriptionRequest unauthorizedSubscriptionRequest = new SubscriptionRequest();

    HttpEntity<SubscriptionRequest> unauthorizedtHttpEntity =
        new HttpEntity<>(
            unauthorizedSubscriptionRequest,
            createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> unauthorizedResponse =
        restTemplate.exchange(
            baseUrl + "/subscribe", HttpMethod.POST, unauthorizedtHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(401), unauthorizedResponse.getStatusCode());
  }

  @Test
  public void testUnsubscribe() throws JsonProcessingException {
    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(new Date());
    themeRepository.save(theme);

    Subscription subscription = new Subscription();
    subscription.setUser(authenticatedUser).setTheme(theme).setCreatedAt(new Date());
    subscriptionRepository.save(subscription);

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUserId(authenticatedUser.getId());
    subscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> httpEntity =
        new HttpEntity<>(
            subscriptionRequest, createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> response =
        restTemplate.exchange(baseUrl + "/unsubscribe", HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

    SubscriptionRequest userNotFoundSubscriptionRequest = new SubscriptionRequest();
    userNotFoundSubscriptionRequest.setUserId(888L);
    userNotFoundSubscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> userNotFountHttpEntity =
        new HttpEntity<>(
            userNotFoundSubscriptionRequest,
            createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> userNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/unsubscribe", HttpMethod.POST, userNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), userNotFoundResponse.getStatusCode());

    SubscriptionRequest themeNotFoundSubscriptionRequest = new SubscriptionRequest();
    themeNotFoundSubscriptionRequest.setUserId(authenticatedUser.getId());
    themeNotFoundSubscriptionRequest.setThemeId(888L);

    HttpEntity<SubscriptionRequest> themeNotFountHttpEntity =
        new HttpEntity<>(
            themeNotFoundSubscriptionRequest,
            createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> themeNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/unsubscribe", HttpMethod.POST, themeNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), themeNotFoundResponse.getStatusCode());

    SubscriptionRequest unauthorizedSubscriptionRequest = new SubscriptionRequest();

    HttpEntity<SubscriptionRequest> unauthorizedtHttpEntity =
        new HttpEntity<>(
            unauthorizedSubscriptionRequest,
            createHeadersWithToken(port, authenticatedUser, restTemplate));

    ResponseEntity<String> unauthorizedResponse =
        restTemplate.exchange(
            baseUrl + "/unsubscribe", HttpMethod.POST, unauthorizedtHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(401), unauthorizedResponse.getStatusCode());
  }
}
