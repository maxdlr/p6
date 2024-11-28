package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.openclassrooms.mddapi.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionControllerTests {

  MockMvc mvc;

  @Mock SubscriptionRepository subscriptionRepository;
  @Mock UserRepository userRepository;
  @Mock ThemeRepository themeRepository;

  @BeforeEach
  public void setup() {
    SubscriptionController subscriptionController =
        new SubscriptionController(userRepository, subscriptionRepository, themeRepository);
    mvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
  }

  @Test
  public void testSubscribe() throws Exception {
    Theme theme = makeTheme(1);
    User user = makeUser(1);

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUserId(user.getId()).setThemeId(theme.getId());

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
    when(subscriptionRepository.existsByUserAndTheme(user, theme)).thenReturn(false);

    String payload = new ObjectMapper().writeValueAsString(subscriptionRequest);

    mvc.perform(
            post("/api/subscriptions/subscribe")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    when(subscriptionRepository.existsByUserAndTheme(user, theme)).thenReturn(true);

    mvc.perform(
            post("/api/subscriptions/subscribe")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("User already subscribed"))
        .andExpect(status().isBadRequest());

    subscriptionRequest.setUserId(2L).setThemeId(1L);
    payload = new ObjectMapper().writeValueAsString(subscriptionRequest);

    mvc.perform(
            post("/api/subscriptions/subscribe")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("User not found"))
        .andExpect(status().isBadRequest());

    subscriptionRequest.setUserId(1L).setThemeId(2L);
    payload = new ObjectMapper().writeValueAsString(subscriptionRequest);

    mvc.perform(
            post("/api/subscriptions/subscribe")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("Theme doesn't exist"))
        .andExpect(status().isBadRequest());
  }
}
