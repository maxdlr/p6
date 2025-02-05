package com.openclassrooms.mddapi.dto;

import java.util.Date;
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

  @NonNull private UserDto author;
  @NonNull private Long articleId;

  private Date createdAt;

  private Date updatedAt;
}
