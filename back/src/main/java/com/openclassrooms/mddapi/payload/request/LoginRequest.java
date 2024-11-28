package com.openclassrooms.mddapi.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class LoginRequest {
  @NotBlank private String email;
  @NotBlank private String password;
}
