package com.koszczi.userswithtasks.domain.users;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class UserValidatorImplTests {
  private UserValidatorImpl validator = new UserValidatorImpl(
      "^[a-zA-Z_0-9]*$", "^[A-Z][a-z]*$", "^[A-Z][a-z]*$");

  private User user;

  @BeforeEach
  public void setup() {
    user = TestUserFactory.buildUser(1L);
  }

  @Test
  public void givenUserNameInvalid_returnsFalse() {
    ReflectionTestUtils.setField(user, "userName", "!!!!");
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenFirstNameInvalid_returnsFalse() {
    ReflectionTestUtils.setField(user, "firstName", "!!!!");
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenLastNameInvalid_returnsFalse() {
    ReflectionTestUtils.setField(user, "lastName", "!!!!");
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenEveryPropertyValid_returnsTrue() {
    assertTrue(validator.isValid(user));
  }

}
