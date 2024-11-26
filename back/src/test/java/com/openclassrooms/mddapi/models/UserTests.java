package com.openclassrooms.mddapi.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class UserTests {
  @Test
  public void testUserEntity() {
    User user = new User();
    user.setUsername("Max");
    user.setPassword("password");
    user.setEmail("max@gmail.com");

    assert user.getUsername().equals("Max");
    assert user.getPassword().equals("password");
    assert user.getEmail().equals("max@gmail.com");
  }
}
