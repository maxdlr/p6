package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubscriptionValidator {

  private final ThemeRepository themeRepository;
  private final SubscriptionRepository subscriptionRepository;
  @Getter private final List<String> errors = new ArrayList<>();
  UserRepository userRepository;

  public SubscriptionValidator(
      UserRepository userRepository,
      ThemeRepository themeRepository,
      SubscriptionRepository subscriptionRepository) {
    this.userRepository = userRepository;
    this.themeRepository = themeRepository;
    this.subscriptionRepository = subscriptionRepository;
  }

  public SubscriptionValidator validateSubscriptionRequest(
      SubscriptionRequest subscriptionRequest) {
    User user = userRepository.findById(subscriptionRequest.getUserId()).orElse(null);
    Theme theme = themeRepository.findById(subscriptionRequest.getThemeId()).orElse(null);

    if (subscriptionRepository.existsByUserAndTheme(user, theme)) {
      errors.add("User already subscribed");
    }

    if (user == null) {
      errors.add("User not found");
    }

    if (theme == null) {
      errors.add("Theme not found");
    }

    return this;
  }

  public SubscriptionValidator validateUnsubscriptionRequest(
      SubscriptionRequest subscriptionRequest) {
    User user = userRepository.findById(subscriptionRequest.getUserId()).orElse(null);
    Theme theme = themeRepository.findById(subscriptionRequest.getThemeId()).orElse(null);

    if (user == null) {
      errors.add("User not found");
    }

    if (theme == null) {
      errors.add("Theme not found");
    }

    if (!subscriptionRepository.existsByUserAndTheme(user, theme)) {
      errors.add("User is not subscribed");
    }

    return this;
  }
}
