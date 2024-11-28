package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final ThemeRepository themeRepository;
  private final SubscriptionService subscriptionService;

  public SubscriptionController(
      UserRepository userRepository,
      SubscriptionRepository subscriptionRepository,
      ThemeRepository themeRepository,
      SubscriptionService subscriptionService) {
    this.userRepository = userRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.themeRepository = themeRepository;
    this.subscriptionService = subscriptionService;
  }

  @PostMapping("/subscribe")
  public ResponseEntity<MessageResponse> subscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {

    List<String> errors =
        subscriptionService.validateSubscriptionRequest(subscriptionRequest).getErrors();

    if (!errors.isEmpty()) {
      return ResponseEntity.badRequest().body(new MessageResponse(errors.toString()));
    }

    User user = userRepository.findById(subscriptionRequest.getUserId()).get();
    Theme theme = themeRepository.findById(subscriptionRequest.getThemeId()).get();

    Subscription subscription = new Subscription();
    subscription.setUser(user).setTheme(theme);

    subscriptionRepository.save(subscription);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/unsubscribe")
  public ResponseEntity<MessageResponse> unsubscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {

    List<String> errors =
        subscriptionService.validateUnsubscriptionRequest(subscriptionRequest).getErrors();

    if (!errors.isEmpty()) {
      return ResponseEntity.badRequest().body(new MessageResponse(errors.toString()));
    }

    User user = userRepository.findById(subscriptionRequest.getUserId()).get();
    Theme theme = themeRepository.findById(subscriptionRequest.getThemeId()).get();
    Optional<Subscription> subscription = subscriptionRepository.findByUserAndTheme(user, theme);

    subscription.ifPresent(subscriptionRepository::removeBy);

    return ResponseEntity.ok().build();
  }
}
