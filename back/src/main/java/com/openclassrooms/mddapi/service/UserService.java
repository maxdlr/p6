package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.*;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignUpRequest;
import com.openclassrooms.mddapi.payload.request.TokenValidationRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final JwtUtils jwtUtils;
  UserRepository userRepository;
  PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtils = jwtUtils;
  }

  public User findUser(LoginRequest loginRequest) {

    if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
      throw new ApiBadPostRequestException("Email or password missing");
    }

    Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

    if (user.isEmpty()) {
      throw new ApiResourceNotFoundException("Cannot find User");
    }

    return user.get();
  }

  public User findUserByEmail(String email) {
    Optional<User> user = userRepository.findByEmail(jwtUtils.getUserNameFromJwtToken(email));

    if (user.isEmpty()) {
      throw new ApiResourceNotFoundException("User not found");
    }
    return user.get();
  }

  public User findUser(String id) {
    try {
      Optional<User> user = userRepository.findById(Long.valueOf(id));

      if (user.isEmpty()) {
        throw new ApiResourceNotFoundException("Cannot find user with id: " + id);
      }

      return user.get();
    } catch (ValidationFailureException e) {
      throw new ApiBadPostRequestException(e.getMessage());
    }
  }

  public void saveUser(SignUpRequest signUpRequest) {
    Boolean isPresent = userRepository.existsByEmail(signUpRequest.getEmail());
    if (isPresent) {
      throw new ApiBadPostRequestException("Email already exists");
    }

    if (signUpRequest.getEmail().isEmpty()) {
      throw new ApiBadPostRequestException("Email missing");
    }

    if (signUpRequest.getUsername().isEmpty()) {
      throw new ApiBadPostRequestException("Username missing");
    }

    if (signUpRequest.getPassword().isEmpty()) {
      throw new ApiBadPostRequestException("Password missing");
    }

    String password = passwordEncoder.encode(signUpRequest.getPassword());
    User user = new User();
    user.setUsername(signUpRequest.getUsername())
        .setEmail(signUpRequest.getEmail())
        .setPassword(password)
        .setCreatedAt(LocalDateTime.now());

    userRepository.save(user);
  }

  public User updateUser(String id, UserDto userDto) {
    try {
      Optional<User> user = userRepository.findById(Long.valueOf(id));

      if (user.isEmpty()) {
        throw new ApiResourceNotFoundException("Cannot find user to update");
      }

      User updatedUser = user.get();

      UserDetails userDetails =
          (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

      if (!Objects.equals(userDetails.getUsername(), updatedUser.getEmail())) {
        throw new UnauthorizedAccessException("Cannot update another user.");
      }

      if (userDto.getEmail() != null) updatedUser.setEmail(userDto.getEmail());
      if (userDto.getPassword() != null) updatedUser.setPassword(userDto.getPassword());
      if (userDto.getUsername() != null) updatedUser.setUsername(userDto.getUsername());
      updatedUser.setUpdatedAt(LocalDateTime.now());

      userRepository.save(updatedUser);
      return updatedUser;
    } catch (ValidationFailureException e) {
      throw new ApiBadPostRequestException(e.getMessage());
    }
  }

  public void validateUser(TokenValidationRequest tokenValidationRequest) {
    boolean isTokenValid = jwtUtils.validateJwtToken(tokenValidationRequest.getToken());

    if (!isTokenValid) {
      throw new ExpiredJwtException("Session is expired");
    }
  }
}
