package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class SignUpRequest {
  @NotBlank
  @Email
  @Size(min = 5, max = 50)
  private String email;

  @NotBlank
  @Size(min = 3, max = 50)
  @NotNull
  private String username;

  @NotBlank
  @NotNull
  @Size(min = 8, max = 50)
  private String password;

  public String getEmail() {
    return email;
  }

  public SignUpRequest setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public SignUpRequest setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public SignUpRequest setPassword(String password) {
    this.password = password;
    return this;
  }
}
