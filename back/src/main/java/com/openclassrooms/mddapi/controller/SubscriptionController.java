package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final ThemeRepository themeRepository;

  public SubscriptionController(
      UserRepository userRepository,
      SubscriptionRepository subscriptionRepository,
      ThemeRepository themeRepository) {
    this.userRepository = userRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.themeRepository = themeRepository;
  }

  @PostMapping("/subscribe")
  public ResponseEntity<MessageResponse> subscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {

    User user = userRepository.findById(subscriptionRequest.getUserId()).orElse(null);
    Theme theme = themeRepository.findById(subscriptionRequest.getThemeId()).orElse(null);

    if (user == null) {
      return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
    }

    if (theme == null) {
      return ResponseEntity.badRequest().body(new MessageResponse("Theme doesn't exist"));
    }

    if (subscriptionRepository.existsByUserAndTheme(user, theme)) {
      return ResponseEntity.badRequest().body(new MessageResponse("User already subscribed"));
    }

    Subscription subscription = new Subscription();
    subscription.setUser(user).setTheme(theme);

    subscriptionRepository.save(subscription);
    return ResponseEntity.ok().build();
  }
}
