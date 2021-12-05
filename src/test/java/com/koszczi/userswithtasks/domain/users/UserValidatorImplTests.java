package com.koszczi.userswithtasks.domain.users;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.koszczi.userswithtasks.domain.users.validation.UserValidationConfig;
import com.koszczi.userswithtasks.domain.users.validation.UserValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class UserValidatorImplTests {
  private UserValidatorImpl validator;

  private User user;

  @BeforeEach
  public void setup() {
    user = TestUserFactory.buildUser(1L);

    UserValidationConfig userValidationConfig = new UserValidationConfig();

    userValidationConfig.setUserNameRegex("^[a-zA-Z_0-9]*$");
    userValidationConfig.setUserNameNullable(false);
    userValidationConfig.setUserNameMaxLength(10);

    userValidationConfig.setFirstNameRegex("^[A-Z][a-z]*$");
    userValidationConfig.setFirstNameNullable(false);
    userValidationConfig.setFirstNameMaxLength(10);

    userValidationConfig.setLastNameRegex("^[A-Z][a-z]*$");
    userValidationConfig.setLastNameNullable(false);
    userValidationConfig.setLastNameMaxLength(10);

    validator = new UserValidatorImpl(userValidationConfig);
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
  public void givenUserNameNullable_returnsFalse() {
    ReflectionTestUtils.setField(user, "userName", null);
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenFirstNameNullable_returnsFalse() {
    ReflectionTestUtils.setField(user, "firstName", null);
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenLastNameNullable_returnsFalse() {
    ReflectionTestUtils.setField(user, "lastName", null);
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenUserNameTooLong_returnsFalse() {
    ReflectionTestUtils.setField(user, "userName", "Qwertyuiopa");
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenFirstNameTooLong_returnsFalse() {
    ReflectionTestUtils.setField(user, "firstName", "Qwertyuiopa");
    assertFalse(validator.isValid(user));
  }

  @Test
  public void givenLastNameTooLong_returnsFalse() {
    ReflectionTestUtils.setField(user, "lastName", "Qwertyuiopa");
    assertFalse(validator.isValid(user));
  }
  
  @Test
  public void givenEveryPropertyValid_returnsTrue() {
    assertTrue(validator.isValid(user));
  }

}
