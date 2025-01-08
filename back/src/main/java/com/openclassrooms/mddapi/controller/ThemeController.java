package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.mapper.ThemeMapper;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/themes")
public class ThemeController {

  private final ThemeMapper themeMapper;
  private final ThemeRepository themeRepository;

  public ThemeController(ThemeMapper themeMapper, ThemeRepository themeRepository) {
    this.themeMapper = themeMapper;
    this.themeRepository = themeRepository;
  }

  @GetMapping
  public ResponseEntity<List<ThemeDto>> browse() {
    List<Theme> themes = themeRepository.findAll();
    return ResponseEntity.ok(themeMapper.toDto(themes));
  }
}
