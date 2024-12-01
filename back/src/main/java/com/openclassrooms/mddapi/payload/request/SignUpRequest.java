package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.Accessors;

@Setter
@Getter
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
}
