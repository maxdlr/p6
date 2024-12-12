package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Objects;
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
public class UserControllerIntegrationTests {
  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;

  private String baseUrl;
  private String jwtToken;

  private User authenticatedUser;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/users";
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
    userRepository.deleteAll();
  }

  private HttpHeaders createHeadersWithToken() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + jwtToken);
    return headers;
  }

  @Test
  public void testGetUserById() {
    HttpEntity<UserDto> httpEntity = new HttpEntity<>(createHeadersWithToken());

    User user = new User();
    user.setUsername("username")
        .setPassword(passwordEncoder.encode("password"))
        .setEmail("email@email.com")
        .setCreatedAt(LocalDateTime.now());

    userRepository.save(user);

    ResponseEntity<UserDto> response =
        restTemplate.exchange(
            baseUrl + "/" + user.getId(), HttpMethod.GET, httpEntity, UserDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(user.getId(), Objects.requireNonNull(response.getBody()).getId());
    assertInstanceOf(UserDto.class, response.getBody());

    ResponseEntity<UserDto> badRequestResponse =
        restTemplate.exchange(baseUrl + "/undefined", HttpMethod.GET, httpEntity, UserDto.class);

    assertEquals(HttpStatus.BAD_REQUEST, badRequestResponse.getStatusCode());

    ResponseEntity<UserDto> notFoundRequestResponse =
        restTemplate.exchange(baseUrl + "/681420685", HttpMethod.GET, httpEntity, UserDto.class);

    assertEquals(HttpStatus.NOT_FOUND, notFoundRequestResponse.getStatusCode());
  }

  @Test
  public void testUpdateUser() {
    UserDto userDto = new UserDto();
    userDto.setUsername("newUsername");

    HttpEntity<UserDto> httpEntity = new HttpEntity<>(userDto, createHeadersWithToken());

    ResponseEntity<UserDto> response =
        restTemplate.exchange(
            baseUrl + "/" + authenticatedUser.getId(), HttpMethod.PUT, httpEntity, UserDto.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(authenticatedUser.getId(), Objects.requireNonNull(response.getBody()).getId());
    assertEquals("newUsername", response.getBody().getUsername());

    ResponseEntity<UserDto> notFoundRequestResponse =
        restTemplate.exchange(baseUrl + "/999999999", HttpMethod.PUT, httpEntity, UserDto.class);

    assertEquals(HttpStatus.NOT_FOUND, notFoundRequestResponse.getStatusCode());

    ResponseEntity<UserDto> badRequestResponse =
        restTemplate.exchange(baseUrl + "/undefined", HttpMethod.PUT, httpEntity, UserDto.class);

    assertEquals(HttpStatus.BAD_REQUEST, badRequestResponse.getStatusCode());
  }
}
