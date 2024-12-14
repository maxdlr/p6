package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.payload.request.SubscriptionRequest;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
  private final SubscriptionService subscriptionService;

  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @PostMapping("/subscribe")
  public ResponseEntity<MessageResponse> subscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    subscriptionService.subscribeUserToTheme(subscriptionRequest);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/unsubscribe")
  public ResponseEntity<MessageResponse> unsubscribe(
      @RequestBody SubscriptionRequest subscriptionRequest) {
    subscriptionService.UnsubscribeUserFromTheme(subscriptionRequest);
    return ResponseEntity.ok().build();
  }
}
