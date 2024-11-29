package com.openclassrooms.mddapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticleDto {
  private Long id;

  @NonNull
  @Size(max = 50)
  private String title;

  @NonNull private Long themeId;
  @NonNull private Long authorId;
  @NonNull private String content;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
