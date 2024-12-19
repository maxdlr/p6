package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.exception.ValidationFailureException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

  private final UserRepository userRepository;
  private final ThemeRepository themeRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final EntityManager entityManager;

  public SubscriptionService(
      UserRepository userRepository,
      ThemeRepository themeRepository,
      SubscriptionRepository subscriptionRepository,
      EntityManager entityManager) {
    this.userRepository = userRepository;
    this.themeRepository = themeRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.entityManager = entityManager;
  }

  public void subscribeUserToTheme(SubscriptionRequest subscriptionRequest) {
    try {
      HashMap<String, Object> validated = validateUserAndTheme(subscriptionRequest);
      User user = (User) validated.get("user");
      Theme theme = (Theme) validated.get("theme");

      if (subscriptionRepository.existsByUserIdAndThemeId(
          subscriptionRequest.getUserId(), subscriptionRequest.getThemeId())) {
        throw new ApiBadPostRequestException("User already subscribed");
      }

      Subscription subscription = new Subscription();
      subscription.setTheme(theme);
      subscription.setUser(user);
      subscription.setCreatedAt(LocalDateTime.now());
      subscriptionRepository.save(subscription);
    } catch (ValidationFailureException e) {
      throw new ApiBadPostRequestException(e.getMessage());
    }
  }

  public void unsubscribeUserFromTheme(SubscriptionRequest subscriptionRequest) {
    HashMap<String, Object> validated = validateUserAndTheme(subscriptionRequest);
    User user = (User) validated.get("user");
    Theme theme = (Theme) validated.get("theme");

    Optional<Subscription> subscription = subscriptionRepository.findByUserAndTheme(user, theme);

    if (subscription.isEmpty()) {
      throw new ApiBadPostRequestException("User is not subscribed");
    }

    subscriptionRepository.delete(subscription.get());
  }

  public HashMap<String, Object> validateUserAndTheme(SubscriptionRequest subscriptionRequest) {
    Optional<User> user = userRepository.findById(subscriptionRequest.getUserId());
    Optional<Theme> theme = themeRepository.findById(subscriptionRequest.getThemeId());

    if (user.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find user");
    }

    if (theme.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find theme");
    }

    HashMap<String, Object> result = new HashMap<>();
    result.put("user", user.get());
    result.put("theme", theme.get());

    return result;
  }

  public List<Long> findAllSubscriptionThemeIdByUser(User user) {
    List<Subscription> subscriptionList = subscriptionRepository.findAllByUser(user);

    return subscriptionList.stream().map(subscription -> subscription.getTheme().getId()).toList();
  }
}
