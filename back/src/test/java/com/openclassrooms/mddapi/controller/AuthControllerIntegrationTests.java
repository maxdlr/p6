package com.openclassrooms.mddapi.controller;

import static com.openclassrooms.mddapi.TestUtils.makeUser;
import static org.junit.jupiter.api.Assertions.*;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignUpRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.Objects;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AuthControllerIntegrationTests {
  @LocalServerPort int port;
  private String baseUrl;
  @Autowired private TestRestTemplate restTemplate;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  public void setup() {
    baseUrl = "http://localhost:" + port + "/api/auth";
  }

  @AfterEach
  public void setUp() {
    userRepository.deleteAll();
  }

  @Test
  public void testLogin() {
    User user = makeUser(1, true);
    userRepository.save(user);

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(user.getEmail());
    loginRequest.setPassword("password1");

    ResponseEntity<String> response =
        restTemplate.postForEntity(baseUrl + "/login", loginRequest, String.class);

    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    assert response.getBody() != null;

    loginRequest.setEmail(user.getEmail() + "404");
    ResponseEntity<String> notFoundRequestResponse =
        restTemplate.postForEntity(baseUrl + "/login", loginRequest, String.class);

    assertEquals(HttpStatusCode.valueOf(404), notFoundRequestResponse.getStatusCode());
  }

  @Test
  public void testRegister() {
    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setEmail("email@email.com1");
    signUpRequest.setPassword("password1");
    signUpRequest.setUsername("username1");

    HttpEntity<SignUpRequest> request = new HttpEntity<>(signUpRequest);

    ResponseEntity<?> response =
        restTemplate.postForEntity(baseUrl + "/register", request, String.class);
    assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    assertNotNull(userRepository.findByEmail("email@email.com1"));

    ResponseEntity<String> badRequestResponse =
        restTemplate.postForEntity(baseUrl + "/register", request, String.class);

    assertEquals(HttpStatusCode.valueOf(400), badRequestResponse.getStatusCode());
    assertTrue(
        Objects.requireNonNull(badRequestResponse.getBody()).contains("Email already exists"));

    SignUpRequest signUpRequest2 = new SignUpRequest();
    signUpRequest2.setEmail("email@email.com1");
    signUpRequest2.setPassword("password1");

    HttpEntity<SignUpRequest> request2 = new HttpEntity<>(signUpRequest2);

    ResponseEntity<String> badRequestResponse2 =
        restTemplate.postForEntity(baseUrl + "/register", request2, String.class);

    assertEquals(HttpStatusCode.valueOf(400), badRequestResponse2.getStatusCode());
  }
}
