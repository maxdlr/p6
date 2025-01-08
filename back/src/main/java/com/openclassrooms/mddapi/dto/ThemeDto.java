package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ThemeDto {
  private Long id;

  @NonNull
  @Size(max = 200)
  private String name;

  @NonNull
  @Size(max = 200)
  private String description;

  private Long articleCount;

  private Date createdAt;

  private Date updatedAt;
}
