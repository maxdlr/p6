package com.openclassrooms.mddapi.mapper;

import static com.openclassrooms.mddapi.TestUtils.makeUser;
import static com.openclassrooms.mddapi.TestUtils.makeUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.models.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserMapperTests {
  private UserMapper userMapper;

  @BeforeEach
  public void setUp() {
    userMapper = new UserMapperImpl();
  }

  @Test
  public void testToDto() {
    User user = makeUser(1, true);
    UserDto userDto = userMapper.toDto(user);

    assertEquals(user.getId(), userDto.getId());
    assertEquals(user.getEmail(), userDto.getEmail());
  }

  @Test
  public void testToEntity() {
    UserDto userDto = makeUserDto(1);
    User user = userMapper.toEntity(userDto);

    assertEquals(user.getId(), userDto.getId());
    assertEquals(user.getEmail(), userDto.getEmail());
  }

  @Test
  public void testToDtoList() {

    List<User> userList = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
      User user = makeUser(1, true);
      userList.add(user);
    }

    List<UserDto> userDtoList = userMapper.toDto(userList);

    assertEquals(10, userDtoList.size());
    assertEquals(userList.getFirst().getId(), userDtoList.getFirst().getId());
    assertEquals(userList.getLast().getId(), userDtoList.getLast().getId());
  }

  @Test
  public void testToEntityList() {

    List<UserDto> userDtoList = new ArrayList<>();

    for (int i = 1; i <= 10; i++) {
      UserDto userDto = makeUserDto(1);
      userDtoList.add(userDto);
    }

    List<User> userList = userMapper.toEntity(userDtoList);

    assertEquals(10, userDtoList.size());
    assertEquals(userList.getFirst().getId(), userDtoList.getFirst().getId());
    assertEquals(userList.getLast().getId(), userDtoList.getLast().getId());
  }
}
