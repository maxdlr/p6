package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentDto;
import com.openclassrooms.mddapi.models.Comment;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.Date;
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

  @Autowired UserMapper userMapper;

  Date newDate = new Date();

  @Mappings({
    @Mapping(
        target = "author",
        expression =
            "java(this.userRepository.findById(commentDto.getAuthor().getId()).orElse(null))"),
    @Mapping(
        target = "article",
        expression =
            "java(this.articleRepository.findById(commentDto.getArticleId()).orElse(null))"),
    @Mapping(target = "updatedAt", expression = "java(newDate)")
  })
  public abstract Comment toEntity(CommentDto commentDto);

  @Mappings({
    @Mapping(target = "author", expression = "java(userMapper.toDto(comment.getAuthor()))"),
    @Mapping(target = "articleId", expression = "java(comment.getArticle().getId())")
  })
  public abstract CommentDto toDto(Comment comment);
}
