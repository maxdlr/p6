package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

  @Mock Authentication authentication;

  @BeforeEach
  public void setup() {
    UserController userController = new UserController(userRepository, userMapper);
    mvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  public void testReadUserById() throws Exception {
    UserDto userDto = makeUserDto(1);
    User user = makeUser(1);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
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

  @Test
  public void testUpdateUser() throws Exception {
    User user = makeUser(1);
    UserDto putRequest =
        new UserDto().setEmail("edited-email@email.com").setUsername("edited-username");
    User editUser =
        new User()
            .setId(user.getId())
            .setUsername(putRequest.getUsername())
            .setEmail(putRequest.getEmail())
            .setPassword(user.getPassword());
    UserDto putResponseBody =
        new UserDto()
            .setId(user.getId())
            .setUsername(putRequest.getUsername())
            .setEmail(putRequest.getEmail())
            .setPassword(user.getPassword());

    UserDetails userDetails =
        new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword());

    SecurityContextHolder.getContext().setAuthentication(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(userMapper.toEntity(putRequest)).thenReturn(user);
    when(userRepository.save(any(User.class))).thenReturn(editUser);
    when(userMapper.toDto(any(User.class))).thenReturn(putResponseBody);

    String payload = new ObjectMapper().writeValueAsString(putRequest);

    mvc.perform(
            put("/api/users/{id}", 1L)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value(putRequest.getUsername()))
        .andExpect(jsonPath("$.email").value(putRequest.getEmail()));

    mvc.perform(
            put("/api/users/{id}", "bad-request")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    mvc.perform(
            put("/api/users/{id}", 2L)
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());

    payload = new ObjectMapper().writeValueAsString(new UserDto());

    mvc.perform(
            put("/api/users/{id}", "bad-request")
                .content(payload)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
