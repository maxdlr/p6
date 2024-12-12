package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.apache.catalina.realm.UserDatabaseRealm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static com.openclassrooms.mddapi.TestUtils.makeUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserControllerIntegrationTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    private String baseUrl;
    private String jwtToken;

    private User authenticatedUser;

    @BeforeEach()
    public void setUp() throws JsonProcessingException {
        baseUrl = "http://localhost:" + port + "/api/users";
        jwtToken = getJwtToken();
    }

    private String getJwtToken() throws JsonProcessingException {
        authenticatedUser = makeUser(1, true);
        userRepository.save(authenticatedUser);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(authenticatedUser.getEmail());
        loginRequest.setPassword("password1");

        ResponseEntity<String> loginResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/auth/login", loginRequest, String.class);
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
        User user = makeUser(2, true);
        userRepository.save(user);

        ResponseEntity<UserDto> response = restTemplate
                .exchange(baseUrl + "/" + user.getId(), HttpMethod.GET, httpEntity, UserDto.class);

        System.out.println(response.getBody());


    }
}
