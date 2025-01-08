package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.SignUpRequest;
import com.openclassrooms.mddapi.payload.request.TokenValidationRequest;
import com.openclassrooms.mddapi.payload.response.JwtResponse;
import com.openclassrooms.mddapi.payload.response.MessageResponse;
import com.openclassrooms.mddapi.security.jwt.JwtUtils;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
import com.openclassrooms.mddapi.service.SubscriptionService;
import com.openclassrooms.mddapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtils jwtUtils;
  private final UserService userService;
  private final UserMapper userMapper;
  private final SubscriptionService subscriptionService;

  public AuthController(
      AuthenticationManager authenticationManager,
      JwtUtils jwtUtils,
      UserService userService,
      UserMapper userMapper,
      SubscriptionService subscriptionService) {
    this.authenticationManager = authenticationManager;
    this.jwtUtils = jwtUtils;
    this.userService = userService;
    this.userMapper = userMapper;
    this.subscriptionService = subscriptionService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    User user = userService.findUser(loginRequest);

    Authentication authentication =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    return ResponseEntity.ok(
        new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            user.getUsername(),
            subscriptionService.findAllSubscriptionThemeIdByUser(user)));
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody SignUpRequest signUpRequest) {
    userService.saveUser(signUpRequest);
    return ResponseEntity.ok().body(new MessageResponse("User registered successfully"));
  }

  @PostMapping("/me")
  public ResponseEntity<UserDto> validateToken(
      @RequestBody TokenValidationRequest tokenValidationRequest) {
    userService.validateUser(tokenValidationRequest);

    System.out.println(tokenValidationRequest);

    User user =
        userService.findUserByEmail(
            jwtUtils.getUserNameFromJwtToken(tokenValidationRequest.getToken()));

    return ResponseEntity.ok(userMapper.toDto(user));
  }
}
