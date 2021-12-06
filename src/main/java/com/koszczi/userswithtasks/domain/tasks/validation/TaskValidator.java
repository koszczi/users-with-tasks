package com.koszczi.userswithtasks.domain.tasks.validation;

import com.koszczi.userswithtasks.domain.tasks.Task;

/**
 * Constains methods for determining the validity of {@link Task} instances from various point of view
 */
public interface TaskValidator {

  /**
   * Performs basic validations on the properties of a {@link Task} instance
   * @param Task {@link Task} instance to validate
   * @return true if the given {@link Task} instance is valid, false otherwise
   */
  boolean isValid(Task Task);

  /**
   * Provides validation details to help building valid {@link Task} entities
   * @return help text
   */
  String help();

}
