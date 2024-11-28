package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.models.Theme;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface ThemeMapper extends EntityMapper<ThemeDto, Theme> {
}
