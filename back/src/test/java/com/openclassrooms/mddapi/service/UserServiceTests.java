package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.UserDto;
import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payload.request.SignUpRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.security.services.UserDetailsImpl;
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

import java.util.Optional;

import static com.openclassrooms.mddapi.TestUtils.makeUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    Authentication authentication;
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    public void testFindUser() {
        User user = makeUser(1, true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findUser("1");
        assertEquals(foundUser.getEmail(), user.getEmail());

        assertThrows(ApiResourceNotFoundException.class,
                () -> userService.findUser("2"));

        assertThrows(ApiBadPostRequestException.class,
                () -> userService.findUser("bad-request"));
    }

    @Test
    public void testSaveUser() {
        SignUpRequest signUpRequest = new SignUpRequest(
                "email@email.com1",
                "username1",
                "password1"
        );

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

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        UserDetails userDetails = new UserDetailsImpl(user.getId(), user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        User updatedUser = userService.updateUser(user.getId().toString(), userDto);

        verify(userRepository).save(any(User.class));

        assertEquals(userDto.getEmail(), updatedUser.getEmail());
    }
}
