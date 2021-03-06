package com.koszczi.userswithtasks.domain.tasks.validation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to collect task validation related configuration properties in order to serve it
 * to a {@link TaskValidator}
 */
@Configuration
@ConfigurationProperties(prefix = "task.validation")
@Getter
@Setter
public class TaskValidationConfig {
  boolean nameNullable;
  int nameMaxLength;
  boolean descriptionNullable;
  int descriptionMaxLength;
  boolean dateTimeNullable;
}
