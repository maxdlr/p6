package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(of = {"id"})
public class ArticleDto {
  private Long id;

  @NonNull
  @Size(max = 50)
  private String title;

  @NonNull private ThemeDto theme;
  @NonNull private UserDto author;
  @NonNull private String content;
  private List<CommentDto> comments;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
