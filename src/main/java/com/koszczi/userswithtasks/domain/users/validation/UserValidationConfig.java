package com.koszczi.userswithtasks.domain.users.validation;

import com.koszczi.userswithtasks.domain.tasks.validation.TaskValidator;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to collect user validation related configuration properties in order to serve it
 * to a {@link UserValidator}
 */
@Configuration
@ConfigurationProperties(prefix = "user.validation")
@Getter
@Setter
public class UserValidationConfig {
  private String userNameRegex;
  private boolean userNameNullable;
  private int userNameMaxLength;

  private String firstNameRegex;
  private boolean firstNameNullable;
  private int firstNameMaxLength;

  private String lastNameRegex;
  private boolean lastNameNullable;
  private int lastNameMaxLength;
}
