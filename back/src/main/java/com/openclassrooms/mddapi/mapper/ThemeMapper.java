package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ThemeDto;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class ThemeMapper implements EntityMapper<ThemeDto, Theme> {

  @Autowired ArticleRepository articleRepository;

  public abstract Theme toEntity(ThemeDto dto);

  @Mappings({
    @Mapping(
        target = "articleCount",
        expression = "java((long) articleRepository.findByTheme(entity).size())"),
  })
  public abstract ThemeDto toDto(Theme entity);
}
