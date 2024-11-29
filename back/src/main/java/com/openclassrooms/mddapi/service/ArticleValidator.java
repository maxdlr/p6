package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleValidator {
  private final UserRepository userRepository;
  private final ThemeRepository themeRepository;
  @Getter List<String> errors = new ArrayList<>();

  public ArticleValidator(UserRepository userRepository, ThemeRepository themeRepository) {
    this.userRepository = userRepository;
    this.themeRepository = themeRepository;
  }

  public ArticleValidator validateDto(ArticleDto articleDto) {
    if (!userRepository.existsById(articleDto.getAuthorId())) {
      errors.add("User not found");
    }

    if (!themeRepository.existsById(articleDto.getThemeId())) {
      errors.add("Theme not found");
    }

    return this;
  }
}
