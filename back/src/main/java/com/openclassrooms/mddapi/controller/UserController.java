package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        try {
            User user =
                    this.userRepository.findById(id).isPresent()
                            ? this.userRepository.findById(id).get()
                            : null;

            if (user == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok().body(this.userMapper.toDto(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        try {
            User foundUser = userRepository.findById(id).orElse(null);

            if (foundUser == null) {
                return ResponseEntity.notFound().build();
            }

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!Objects.equals(userDetails.getUsername(), foundUser.getEmail())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = userMapper.toEntity(userDto);
            user.setId(id);
            User savedUser = userRepository.save(user);

            return ResponseEntity.ok().body(userMapper.toDto(savedUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
