package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

public class UserMapperTests {
  private UserMapper userMapper;

  @BeforeEach
  public void setup() {
    this.userMapper =
        new UserMapper() {
          @Override
          public User toEntity(UserDto userDto) {
            return null;
          }

          @Override
          public UserDto toDto(User user) {
            return null;
          }

          @Override
          public List<User> toEntity(List<UserDto> dtoList) {
            return List.of();
          }

          @Override
          public List<UserDto> toDto(List<User> entityList) {
            return List.of();
          }
        };
  }

  @Test
  public void testUserToUserDto() {
    User user = new User();
    user.setId(1L)
        .setUsername("max")
        .setPassword("password")
        .setEmail("max@mail.com")
        .setCreatedAt(LocalDateTime.now())
        .setUpdatedAt(LocalDateTime.now());

    UserDto userDto = userMapper.toDto(user);

    assert userDto.getUsername().equals("max");
  }
}
