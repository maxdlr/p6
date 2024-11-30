package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
public class CommentDto {
  private Long id;

  @NonNull private String content;

  @NonNull private Long authorId;
  @NonNull private Long articleId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
