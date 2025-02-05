package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.Date;
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

  @Autowired CommentRepository commentRepository;

  @Autowired ThemeMapper themeMapper;
  @Autowired UserMapper userMapper;
  @Autowired CommentMapper commentMapper;

  Date newDate = new Date();

  @Mappings({
    @Mapping(
        target = "author",
        expression =
            "java(this.userRepository.findById(articleDto.getAuthor().getId()).orElse(null))"),
    @Mapping(
        target = "theme",
        expression =
            "java(this.themeRepository.findById(articleDto.getTheme().getId()).orElse(null))"),
    @Mapping(target = "updatedAt", expression = "java(newDate)")
  })
  public abstract Article toEntity(ArticleDto articleDto);

  @Mappings({
    @Mapping(target = "author", expression = "java(userMapper.toDto(article.getAuthor()))"),
    @Mapping(target = "theme", expression = "java(themeMapper.toDto(article.getTheme()))"),
    @Mapping(
        target = "comments",
        expression =
            "java(commentMapper.toDto(commentRepository.findAllByArticleOrderByCreatedAtDesc(article)))")
  })
  public abstract ArticleDto toDto(Article article);
}
