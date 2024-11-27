package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestUtils;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SignUpRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

  MockMvc mvc;

  @Mock UserRepository userRepository;
  @Mock PasswordEncoder passwordEncoder;

  @BeforeEach
  public void setup() {
    AuthController authController = new AuthController(userRepository, passwordEncoder);
    mvc = MockMvcBuilders.standaloneSetup(authController).build();
  }

  @Test
  public void testRegisterUser() throws Exception {
    User user = TestUtils.makeUser(1);

    when(userRepository.save(any(User.class))).thenReturn(user);

    String email = "email@email.com1";
    String password = "password1";
    String username = "username1";

    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setEmail(email).setUsername(username).setPassword(password);

    when(passwordEncoder.encode(password)).thenReturn("encoded-password");

    String payload = new ObjectMapper().writeValueAsString(signUpRequest);

    when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

    mvc.perform(
            post("/api/auth/register")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Email already exists"));

    when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);

    mvc.perform(
            post("/api/auth/register")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("User registered successfully"));

    payload = new ObjectMapper().writeValueAsString(new SignUpRequest());

    mvc.perform(
            post("/api/auth/register")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
