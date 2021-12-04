package com.koszczi.userswithtasks.domain.users;

/**
 * Constains methods for determining the validity of {@link User} instances from various point of view
 */
public interface UserValidator {

  /**
   * Performs basic validations on the properties of a {@link User} instance
   * @param user {@link User} instance to validate
   * @return true if the given {@link User} instance is valid, false otherwise
   */
  boolean isValid(User user);

}
