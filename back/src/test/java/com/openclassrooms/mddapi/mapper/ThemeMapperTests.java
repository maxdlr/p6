package com.openclassrooms.mddapi.mapper;

import static com.openclassrooms.mddapi.TestUtils.makeTheme;
import static com.openclassrooms.mddapi.TestUtils.makeThemeDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.models.Theme;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ThemeMapperTests {
  private ThemeMapper themeMapper;

  @BeforeEach
  public void setUp() {
    themeMapper = new ThemeMapperImpl();
  }

  @Test
  public void testToDto() {
    Theme Theme = makeTheme(1);
    ThemeDto ThemeDto = themeMapper.toDto(Theme);

    assertEquals(Theme.getId(), ThemeDto.getId());
    assertEquals(Theme.getName(), ThemeDto.getName());
  }

  @Test
  public void testToEntity() {
    ThemeDto ThemeDto = makeThemeDto(1);
    Theme Theme = themeMapper.toEntity(ThemeDto);

    assertEquals(Theme.getId(), ThemeDto.getId());
    assertEquals(Theme.getName(), ThemeDto.getName());
  }

  @Test
  public void testToDtoList() {

    List<Theme> ThemeList = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
      Theme Theme = makeTheme(1);
      ThemeList.add(Theme);
    }

    List<ThemeDto> ThemeDtoList = themeMapper.toDto(ThemeList);

    assertEquals(10, ThemeDtoList.size());
    assertEquals(ThemeList.getFirst().getId(), ThemeDtoList.getFirst().getId());
    assertEquals(ThemeList.getLast().getId(), ThemeDtoList.getLast().getId());
  }

  @Test
  public void testToEntityList() {

    List<ThemeDto> ThemeDtoList = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
      ThemeDto ThemeDto = makeThemeDto(1);
      ThemeDtoList.add(ThemeDto);
    }

    List<Theme> ThemeList = themeMapper.toEntity(ThemeDtoList);

    assertEquals(10, ThemeDtoList.size());
    assertEquals(ThemeList.getFirst().getId(), ThemeDtoList.getFirst().getId());
    assertEquals(ThemeList.getLast().getId(), ThemeDtoList.getLast().getId());
  }
}
