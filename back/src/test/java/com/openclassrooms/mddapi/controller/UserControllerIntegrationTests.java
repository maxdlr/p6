package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.createHeadersWithToken;
import static com.openclassrooms.mddapi.TestUtils.getAuthenticatedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
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

  private User authenticatedUser;

  @BeforeEach()
  public void setUp() throws JsonProcessingException {
    baseUrl = "http://localhost:" + port + "/api/users";
    authenticatedUser = getAuthenticatedUser(passwordEncoder, userRepository);
  }

  @AfterEach
  public void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  public void testGetUserById() throws JsonProcessingException {
    HttpEntity<UserDto> httpEntity =
        new HttpEntity<>(createHeadersWithToken(port, authenticatedUser, restTemplate));

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
  public void testUpdateUser() throws JsonProcessingException {
    UserDto userDto = new UserDto();
    userDto.setUsername("newUsername");

    HttpEntity<UserDto> httpEntity =
        new HttpEntity<>(userDto, createHeadersWithToken(port, authenticatedUser, restTemplate));

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
