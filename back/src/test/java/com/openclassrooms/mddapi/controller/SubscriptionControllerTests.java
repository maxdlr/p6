package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.service.SubscriptionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.swing.text.html.Option;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
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
    SubscriptionValidator subscriptionValidator =
        new SubscriptionValidator(userRepository, themeRepository, subscriptionRepository);

    SubscriptionController subscriptionController =
        new SubscriptionController(
            userRepository, subscriptionRepository, themeRepository, subscriptionValidator);
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

    String userAlreadySubscribedResponseBody =
        mvc.perform(
                post("/api/subscriptions/subscribe")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(userAlreadySubscribedResponseBody.contains("User already subscribed"));

    subscriptionRequest.setUserId(2L).setThemeId(1L);
    payload = new ObjectMapper().writeValueAsString(subscriptionRequest);

    String userNotFoundResponseBody =
        mvc.perform(
                post("/api/subscriptions/subscribe")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(userNotFoundResponseBody.contains("User not found"));

    subscriptionRequest.setUserId(1L).setThemeId(2L);
    payload = new ObjectMapper().writeValueAsString(subscriptionRequest);

    String themeNotFoundResponseBody =
        mvc.perform(
                post("/api/subscriptions/subscribe")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(themeNotFoundResponseBody.contains("Theme not found"));
  }

  @Test
  public void testUnsubscribe() throws Exception {
    Theme theme = makeTheme(1);
    User user = makeUser(1);
    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUserId(user.getId()).setThemeId(theme.getId());

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(themeRepository.findById(1L)).thenReturn(Optional.of(theme));
    when(subscriptionRepository.existsByUserAndTheme(user, theme)).thenReturn(true);
    when(subscriptionRepository.findByUserAndTheme(user, theme))
        .thenReturn(Optional.of(makeSubscription(1, theme, user)));

    String payload = new ObjectMapper().writeValueAsString(subscriptionRequest);

    mvc.perform(
            post("/api/subscriptions/unsubscribe")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    when(subscriptionRepository.existsByUserAndTheme(user, theme)).thenReturn(false);

    String userNotSubscribedResponseBody =
        mvc.perform(
                post("/api/subscriptions/unsubscribe")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(userNotSubscribedResponseBody.contains("User is not subscribed"));

    subscriptionRequest.setUserId(2L).setThemeId(1L);
    payload = new ObjectMapper().writeValueAsString(subscriptionRequest);
    String userNotFoundResponseBody =
        mvc.perform(
                post("/api/subscriptions/unsubscribe")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(userNotFoundResponseBody.contains("User not found"));

    subscriptionRequest.setUserId(1L).setThemeId(2L);
    payload = new ObjectMapper().writeValueAsString(subscriptionRequest);
    String themeNotFoundResponseBody =
        mvc.perform(
                post("/api/subscriptions/unsubscribe")
                    .content(payload)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(themeNotFoundResponseBody.contains("Theme not found"));
  }
}
