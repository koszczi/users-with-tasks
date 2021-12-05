package com.koszczi.userswithtasks.domain.users.validation;

import com.koszczi.userswithtasks.domain.users.User;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorImpl implements UserValidator {
  private final Pattern userNamePattern;
  private final Pattern firstNamePattern;
  private final Pattern lastNamePattern;
  private final UserValidationConfig validationConfig;

  public UserValidatorImpl(
    UserValidationConfig userValidationConfig
  ) {
    this.validationConfig = userValidationConfig;

    userNamePattern = Pattern.compile(userValidationConfig.getUserNameRegex());
    firstNamePattern = Pattern.compile(userValidationConfig.getFirstNameRegex());
    lastNamePattern = Pattern.compile(userValidationConfig.getLastNameRegex());
  }

  @Override
  public boolean isValid(User user) {
    return nullabilitiesAreOkFor(user) && lengthsAreOkFor(user) && pattersAreOkFor(user);
  }

  private boolean pattersAreOkFor(User user) {
    return userNamePattern.matcher(user.getUserName()).matches()
        && firstNamePattern.matcher(user.getFirstName()).matches()
        && lastNamePattern.matcher(user.getLastName()).matches();
  }

  private boolean lengthsAreOkFor(User user) {
    return user.getUserName().length() <= validationConfig.getUserNameMaxLength()
      && user.getFirstName().length() <= validationConfig.getFirstNameMaxLength()
      && user.getLastName().length() <= validationConfig.getLastNameMaxLength();
  }

  private boolean nullabilitiesAreOkFor(User user) {
    return nullabilityIsOk(user.getUserName(), validationConfig.isUserNameNullable())
        && nullabilityIsOk(user.getFirstName(), validationConfig.isFirstNameNullable())
        && nullabilityIsOk(user.getLastName(), validationConfig.isLastNameNullable());
  }

  private boolean nullabilityIsOk(String field, boolean nullability) {
    if (nullability != true) return field != null;
    return true;
  }
}
