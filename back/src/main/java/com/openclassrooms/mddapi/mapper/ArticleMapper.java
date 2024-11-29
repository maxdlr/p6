package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class ArticleMapper implements EntityMapper<ArticleDto, Article> {

  @Autowired ThemeRepository themeRepository;

  @Autowired UserRepository userRepository;

  @Mappings({
    @Mapping(
        target = "author",
        expression = "java(this.userRepository.findById(articleDto.getAuthorId()).orElse(null))"),
    @Mapping(
        target = "theme",
        expression = "java(this.themeRepository.findById(articleDto.getThemeId()).orElse(null))"),
  })
  public abstract Article toEntity(ArticleDto articleDto);

  @Mappings({
    @Mapping(target = "authorId", expression = "java(article.getAuthor().getId())"),
    @Mapping(target = "themeId", expression = "java(article.getTheme().getId())")
  })
  public abstract ArticleDto toDto(Article article);
}
