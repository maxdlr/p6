package com.openclassrooms.mddapi.service;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTests {

  @Mock private UserRepository userRepository;
  @Mock private ThemeRepository themeRepository;
  @Mock private SubscriptionRepository subscriptionRepository;

  private SubscriptionService subscriptionService;

  @BeforeEach
  public void setUp() {
    subscriptionService =
        new SubscriptionService(userRepository, themeRepository, subscriptionRepository);
  }

  @Test
  public void testSubscribeToTheme() {

    Theme theme = makeTheme(1);
    User user = makeUser(1, true);

    User subscriber = makeUser(3, true);
    Theme subscribed = makeTheme(3);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));

    when(subscriptionRepository.existsByUserIdAndThemeId(user.getId(), theme.getId()))
        .thenReturn(false);

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest(theme.getId(), user.getId());
    subscriptionService.subscribeUserToTheme(subscriptionRequest);

    verify(subscriptionRepository).save(any(Subscription.class));

    assertThrows(
        ApiResourceNotFoundException.class,
        () -> subscriptionService.subscribeUserToTheme(new SubscriptionRequest(1L, 2L)));

    assertThrows(
        ApiResourceNotFoundException.class,
        () -> subscriptionService.subscribeUserToTheme(new SubscriptionRequest(2L, 1L)));

    when(userRepository.findById(subscriber.getId())).thenReturn(Optional.of(subscriber));
    when(themeRepository.findById(subscribed.getId())).thenReturn(Optional.of(subscribed));

    when(subscriptionRepository.existsByUserIdAndThemeId(subscriber.getId(), subscribed.getId()))
        .thenReturn(true);

    assertThrows(
        ApiBadPostRequestException.class,
        () -> subscriptionService.subscribeUserToTheme(new SubscriptionRequest(3L, 3L)));
  }

  @Test
  public void testUnsubscribeToTheme() {

    Theme theme = makeTheme(1);
    User user = makeUser(1, true);
    Subscription subscription = makeSubscription(1, theme, user);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(themeRepository.findById(theme.getId())).thenReturn(Optional.of(theme));

    when(subscriptionRepository.findByUserAndTheme(user, theme))
        .thenReturn(Optional.of(subscription));

    SubscriptionRequest subscriptionRequest = new SubscriptionRequest(theme.getId(), user.getId());
    subscriptionService.unsubscribeUserFromTheme(subscriptionRequest);

    verify(subscriptionRepository).delete(subscription);

    assertThrows(
        ApiResourceNotFoundException.class,
        () -> subscriptionService.unsubscribeUserFromTheme(new SubscriptionRequest(1L, 2L)));

    assertThrows(
        ApiResourceNotFoundException.class,
        () -> subscriptionService.unsubscribeUserFromTheme(new SubscriptionRequest(2L, 1L)));

    when(subscriptionRepository.findByUserAndTheme(any(User.class), any(Theme.class)))
        .thenReturn(Optional.empty());

    assertThrows(
        ApiBadPostRequestException.class,
        () -> subscriptionService.unsubscribeUserFromTheme(subscriptionRequest));
  }
}
