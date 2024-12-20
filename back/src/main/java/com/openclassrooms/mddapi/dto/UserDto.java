package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserDto {
  private Long id;

  @Size(max = 50)
  @Email
  private String email;

  @Size(max = 20)
  private String username;

  @JsonIgnore
  @Size(max = 120)
  private String password;

  private List<Long> subscriptionThemes;

  private Date createdAt;

  private Date updatedAt;
}
