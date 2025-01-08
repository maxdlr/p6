package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.service.SubscriptionService;
import com.openclassrooms.mddapi.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
  private final SubscriptionService subscriptionService;
  private final UserService userService;

  public SubscriptionController(SubscriptionService subscriptionService, UserService userService) {
    this.subscriptionService = subscriptionService;
    this.userService = userService;
  }

  @PostMapping("/subscribe")
  public ResponseEntity<List<Long>> subscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    subscriptionService.subscribeUserToTheme(subscriptionRequest);
    return ResponseEntity.ok().body(getCurrentUserThemeIds(subscriptionRequest));
  }

  @PostMapping("/unsubscribe")
  public ResponseEntity<List<Long>> unsubscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    subscriptionService.unsubscribeUserFromTheme(subscriptionRequest);
    return ResponseEntity.ok().body(getCurrentUserThemeIds(subscriptionRequest));
  }

  private List<Long> getCurrentUserThemeIds(SubscriptionRequest subscriptionRequest) {
    User user = userService.findUser(subscriptionRequest.getUserId());
    return subscriptionService.findAllSubscriptionThemeIdByUser(user);
  }
}
