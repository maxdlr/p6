package com.openclassrooms.mddapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.time.LocalDateTime;
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
  private String jwtToken;

  private User authenticatedUser;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private ArticleRepository articleRepository;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/subscriptions";
    jwtToken = getJwtToken();
  }

  private String getJwtToken() throws JsonProcessingException {
    authenticatedUser = new User();
    authenticatedUser
        .setUsername("username1")
        .setPassword(passwordEncoder.encode("password1"))
        .setEmail("email@email.com1")
        .setCreatedAt(LocalDateTime.now());

    userRepository.save(authenticatedUser);

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

  @AfterEach
  public void tearDown() {
    articleRepository.deleteAll();
    subscriptionRepository.deleteAll();
    userRepository.deleteAll();
    themeRepository.deleteAll();
  }

  private HttpHeaders createHeadersWithToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + jwtToken);
    return headers;
  }

  @Test
  public void testSubscribe() {
    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(LocalDateTime.now());
    themeRepository.save(theme);

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUserId(authenticatedUser.getId());
    subscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> httpEntity =
        new HttpEntity<>(subscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> response =
        restTemplate.exchange(baseUrl + "/subscribe", HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

    SubscriptionRequest userNotFoundSubscriptionRequest = new SubscriptionRequest();
    userNotFoundSubscriptionRequest.setUserId(888L);
    userNotFoundSubscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> userNotFountHttpEntity =
        new HttpEntity<>(userNotFoundSubscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> userNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/subscribe", HttpMethod.POST, userNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), userNotFoundResponse.getStatusCode());

    SubscriptionRequest themeNotFoundSubscriptionRequest = new SubscriptionRequest();
    themeNotFoundSubscriptionRequest.setUserId(authenticatedUser.getId());
    themeNotFoundSubscriptionRequest.setThemeId(888L);

    HttpEntity<SubscriptionRequest> themeNotFountHttpEntity =
        new HttpEntity<>(themeNotFoundSubscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> themeNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/subscribe", HttpMethod.POST, themeNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), themeNotFoundResponse.getStatusCode());

    SubscriptionRequest badRequestSubscriptionRequest = new SubscriptionRequest();

    HttpEntity<SubscriptionRequest> badRequesttHttpEntity =
        new HttpEntity<>(badRequestSubscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> badRequestResponse =
        restTemplate.exchange(
            baseUrl + "/subscribe", HttpMethod.POST, badRequesttHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(400), badRequestResponse.getStatusCode());
  }

  @Test
  public void testUnsubscribe() {
    Theme theme = new Theme();
    theme.setName("name").setCreatedAt(LocalDateTime.now());
    themeRepository.save(theme);

    Subscription subscription = new Subscription();
    subscription.setUser(authenticatedUser).setTheme(theme).setCreatedAt(LocalDateTime.now());
    subscriptionRepository.save(subscription);

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUserId(authenticatedUser.getId());
    subscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> httpEntity =
        new HttpEntity<>(subscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> response =
        restTemplate.exchange(baseUrl + "/unsubscribe", HttpMethod.POST, httpEntity, String.class);

    assertEquals(HttpStatus.valueOf(200), response.getStatusCode());

    SubscriptionRequest userNotFoundSubscriptionRequest = new SubscriptionRequest();
    userNotFoundSubscriptionRequest.setUserId(888L);
    userNotFoundSubscriptionRequest.setThemeId(theme.getId());

    HttpEntity<SubscriptionRequest> userNotFountHttpEntity =
        new HttpEntity<>(userNotFoundSubscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> userNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/unsubscribe", HttpMethod.POST, userNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), userNotFoundResponse.getStatusCode());

    SubscriptionRequest themeNotFoundSubscriptionRequest = new SubscriptionRequest();
    themeNotFoundSubscriptionRequest.setUserId(authenticatedUser.getId());
    themeNotFoundSubscriptionRequest.setThemeId(888L);

    HttpEntity<SubscriptionRequest> themeNotFountHttpEntity =
        new HttpEntity<>(themeNotFoundSubscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> themeNotFoundResponse =
        restTemplate.exchange(
            baseUrl + "/unsubscribe", HttpMethod.POST, themeNotFountHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(404), themeNotFoundResponse.getStatusCode());

    SubscriptionRequest badRequestSubscriptionRequest = new SubscriptionRequest();

    HttpEntity<SubscriptionRequest> badRequesttHttpEntity =
        new HttpEntity<>(badRequestSubscriptionRequest, createHeadersWithToken());

    ResponseEntity<String> badRequestResponse =
        restTemplate.exchange(
            baseUrl + "/unsubscribe", HttpMethod.POST, badRequesttHttpEntity, String.class);

    assertEquals(HttpStatus.valueOf(400), badRequestResponse.getStatusCode());
  }
}
