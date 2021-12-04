package com.koszczi.userswithtasks.domain.users;

import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorImpl implements UserValidator {
  private final Pattern userNamePattern;
  private final Pattern firstNamePattern;
  private final Pattern lastNamePattern;

  public UserValidatorImpl(
      @Value("${user.user-name.validation-regex}") String userNameRegex,
      @Value("${user.first-name.validation-regex}") String firstNameRegex,
      @Value("${user.last-name.validation-regex}") String lastNameRegex
  ) {
    userNamePattern = Pattern.compile(userNameRegex);
    firstNamePattern = Pattern.compile(firstNameRegex);
    lastNamePattern = Pattern.compile(lastNameRegex);
  }

  @Override
  public boolean isValid(User user) {
    return userNamePattern.matcher(user.getUserName()).matches()
        && firstNamePattern.matcher(user.getFirstName()).matches()
        && lastNamePattern.matcher(user.getLastName()).matches();
  }

}
