package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ThemeDto {
    private Long id;

    @NonNull
    @Size(max = 200)
    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
