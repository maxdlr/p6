package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class CommentMapper implements EntityMapper<CommentDto, Comment> {

  @Autowired ArticleRepository articleRepository;

  @Autowired UserRepository userRepository;

  @Mappings({
    @Mapping(
        target = "author",
        expression = "java(this.userRepository.findById(commentDto.getAuthorId()).orElse(null))"),
    @Mapping(
        target = "article",
        expression =
            "java(this.articleRepository.findById(commentDto.getArticleId()).orElse(null))"),
  })
  public abstract Comment toEntity(CommentDto commentDto);

  @Mappings({
    @Mapping(target = "authorId", expression = "java(article.getAuthor().getId())"),
    @Mapping(target = "articleId", expression = "java(article.getArticle().getId())")
  })
  public abstract CommentDto toDto(Comment article);
}
