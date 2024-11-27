package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static com.openclassrooms.mddapi.TestUtils.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

  MockMvc mvc;

  @Mock UserRepository userRepository;

  @Mock UserMapper userMapper;

  @BeforeEach
  public void setup() {
    UserController userController = new UserController(userRepository, userMapper);
    mvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  public void testReadUserById() throws Exception {
    UserDto userDto = makeUserDto(1);
    User user = makeUser(1);

    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
    when(userMapper.toDto(any(User.class))).thenReturn(userDto);

    mvc.perform(
            get("/api/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));

    mvc.perform(
            get("/api/users/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    mvc.perform(
            get("/api/users/{id}", "bad-request")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
