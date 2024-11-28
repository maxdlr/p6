package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.models.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static com.openclassrooms.mddapi.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class ThemeControllerTests {

    MockMvc mvc;

    @Mock
    ThemeMapper themeMapper;

    @Mock
    ThemeRepository themeRepository;

    @BeforeEach
    void setup() {
        ThemeController themeController = new ThemeController(themeMapper, themeRepository);
        mvc = MockMvcBuilders.standaloneSetup().build();
    }

    @Test
    public void testBrowseListOfTheme() throws Exception {
        List<Theme> themes;
        List<ThemeDto> themeDtos;

        for (int i = 0; i < 10; i++) {
            Theme theme = makeTheme(i);
            ThemeDto themeDto = makeThemeDto(i);
            themes.add(theme);
            themeDtos.add(themeDto);
        }

        when(themeMapper.toDto(themes)).thenReturn(themeDtos);

        mvc.perform(
                        get("/api/themes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(themes.getFirst().getId()))
                .andExpect(jsonPath("$[" + (themes.size() - 1) + "].id").value(themes.get(themes.size() - 1).getId()));

        when(themeMapper.toDto(any(List.class))).thenReturn(new List<ThemeDto>());

        mvc.perform(
                        get("/api/themes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("[]"));
    }
}
