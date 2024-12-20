package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.service.SubscriptionService;
import java.util.Date;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class UserMapper implements EntityMapper<UserDto, User> {
  public Date newDate = new Date();
  @Autowired SubscriptionService subscriptionService;

  @Mappings({
    @Mapping(
        target = "subscriptionThemes",
        expression = "java(this.subscriptionService.findAllSubscriptionThemeIdByUser(user))"),
  })
  public abstract UserDto toDto(User user);

  @Mapping(target = "updatedAt", expression = "java(newDate)")
  public abstract User toEntity(UserDto userDto);
}
