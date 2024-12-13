package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserController(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
    try {
      User user =
          this.userRepository.findById(Long.valueOf(id)).isPresent()
              ? this.userRepository.findById(Long.valueOf(id)).get()
              : null;

      if (user == null) return ResponseEntity.notFound().build();
      return ResponseEntity.ok().body(this.userMapper.toDto(user));
    } catch (NumberFormatException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDto> updateUser(
      @PathVariable String id, @Valid @RequestBody UserDto userDto) {
    try {
      User user = userRepository.findById(Long.valueOf(id)).orElse(null);

      if (user == null) {
        return ResponseEntity.notFound().build();
      }

      UserDetails userDetails =
          (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      if (!Objects.equals(userDetails.getUsername(), user.getEmail())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
      }

      if (userDto.getEmail() != null) user.setEmail(userDto.getEmail());
      if (userDto.getPassword() != null) user.setPassword(userDto.getPassword());
      if (userDto.getUsername() != null) user.setUsername(userDto.getUsername());
      user.setUpdatedAt(LocalDateTime.now());

      this.userRepository.save(user);

      return ResponseEntity.ok().body(userMapper.toDto(user));
    } catch (NumberFormatException e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
