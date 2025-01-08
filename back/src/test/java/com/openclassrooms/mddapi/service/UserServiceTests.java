package com.openclassrooms.mddapi.service;

import static com.openclassrooms.mddapi.TestUtils.makeUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.exception.UnauthorizedAccessException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignUpRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
  @Mock Authentication authentication;
  private UserService userService;
  @Mock private UserRepository userRepository;
  @Mock private JwtUtils jwtUtils;

  @BeforeEach
  public void setUp() {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    userService = new UserService(userRepository, jwtUtils, passwordEncoder);
  }

  @Test
  public void testFindUserByString() {
    User user = makeUser(1, true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));

    User foundUser = userService.findUser("1");
    assertEquals(foundUser.getEmail(), user.getEmail());

    assertThrows(ApiResourceNotFoundException.class, () -> userService.findUser("2"));

    assertThrows(ApiBadPostRequestException.class, () -> userService.findUser("bad-request"));
  }

  @Test
  public void testFindUserByLoginRequest() {
    User user = makeUser(1, true);
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setEmail(user.getEmail());
    loginRequest.setPassword("password1");

    User foundUser = userService.findUser(loginRequest);
    assertEquals(foundUser.getEmail(), user.getEmail());

    assertThrows(ApiBadPostRequestException.class, () -> userService.findUser(new LoginRequest()));

    when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

    assertThrows(ApiResourceNotFoundException.class, () -> userService.findUser(loginRequest));

    assertThrows(ApiBadPostRequestException.class, () -> userService.findUser("bad-request"));
  }

  @Test
  public void testSaveUser() {
    SignUpRequest signUpRequest = new SignUpRequest("email@email.com1", "username1", "password1");

    userService.saveUser(signUpRequest);
    verify(userRepository).save(any(User.class));

    SignUpRequest badRequest = new SignUpRequest();

    assertThrows(ApiBadPostRequestException.class, () -> userService.saveUser(badRequest));
  }

  @Test
  public void testUpdateUser() {
    UserDto userDto = new UserDto();
    userDto.setEmail("edited-email@email.com");
    User user = makeUser(98654, true);
    User wrongUser = makeUser(654654, true);

    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    UserDetails userDetails =
        new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    when(authentication.getPrincipal()).thenReturn(userDetails);

    User updatedUser = userService.updateUser(user.getId().toString(), userDto);

    verify(userRepository).save(any(User.class));
    assertEquals(userDto.getEmail(), updatedUser.getEmail());

    assertThrows(
        UnauthorizedAccessException.class,
        () -> userService.updateUser(wrongUser.getId().toString(), userDto));
  }
}
