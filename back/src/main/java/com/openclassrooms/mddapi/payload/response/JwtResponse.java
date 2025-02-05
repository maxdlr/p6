package com.openclassrooms.mddapi.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String email;
  private String username;
  private List<Long> subscriptionThemes;

  public JwtResponse(
      String accessToken, Long id, String email, String username, List<Long> subscriptionThemes) {
    this.token = accessToken;
    this.id = id;
    this.email = email;
    this.username = username;
    this.subscriptionThemes = subscriptionThemes;
  }
}
