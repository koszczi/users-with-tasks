package com.koszczi.userswithtasks.domain.users.validation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
