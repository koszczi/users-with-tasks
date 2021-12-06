package com.koszczi.userswithtasks.domain.users.validation;

import com.koszczi.userswithtasks.domain.users.User;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class UserValidatorImpl implements UserValidator {
  private static final String DUMMY_STRING = "X";
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
    return (user.getUserName() != null ? user.getUserName() : DUMMY_STRING).length() <= validationConfig.getUserNameMaxLength()
      && (user.getUserName() != null ? user.getFirstName() : DUMMY_STRING).length() <= validationConfig.getFirstNameMaxLength()
      && (user.getUserName() != null ? user.getLastName() : DUMMY_STRING).length() <= validationConfig.getLastNameMaxLength();
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

  public String help() {
    return new StringBuffer()
        .append("userName: ")
        .append("\n")
        .append("nullable: ").append(validationConfig.isUserNameNullable())
        .append("\n")
        .append("max length: ").append(validationConfig.getUserNameMaxLength())
        .append("\n")
        .append("validation regex: ").append(validationConfig.getUserNameRegex())
        .append("\n\n")
        .append("firstName: ")
        .append("\n")
        .append("nullable: ").append(validationConfig.isFirstNameNullable())
        .append("\n")
        .append("max length: ").append(validationConfig.getFirstNameMaxLength())
        .append("\n")
        .append("validation regex: ").append(validationConfig.getFirstNameRegex())
        .append("\n\n")
        .append("lastName: ")
        .append("\n")
        .append("nullable: ").append(validationConfig.isLastNameNullable())
        .append("\n")
        .append("max length: ").append(validationConfig.getLastNameMaxLength())
        .append("\n")
        .append("validation regex: ").append(validationConfig.getLastNameRegex())
        .toString();

  }
}
