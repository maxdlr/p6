package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static com.openclassrooms.mddapi.TestUtils.createHeadersWithToken;
import static com.openclassrooms.mddapi.TestUtils.getAuthenticatedUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ThemeControllerIntegrationTests {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl;
    private User authenticatedUser;

    @BeforeEach()
    public void setUp() throws JsonProcessingException {
        baseUrl = "http://localhost:" + port + "/api/themes";
        authenticatedUser = getAuthenticatedUser(passwordEncoder, userRepository);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        themeRepository.deleteAll();
    }

    @Test
    public void testGetAllThemes() throws JsonProcessingException {
        HttpEntity<?> httpEntity =
                new HttpEntity<>(createHeadersWithToken(port, authenticatedUser, restTemplate));

        for (int i = 0; i < 10; i++) {
            Theme theme = new Theme();
            theme.setName("name").setDescription("description").setCreatedAt(LocalDateTime.now());
            themeRepository.save(theme);
        }

        ResponseEntity<String> response =
                restTemplate.exchange(baseUrl, HttpMethod.GET, httpEntity, String.class);

        assertEquals(HttpStatus.valueOf(200), response.getStatusCode());
        assertEquals(10, new ObjectMapper().readTree(response.getBody()).size());
    }
}
