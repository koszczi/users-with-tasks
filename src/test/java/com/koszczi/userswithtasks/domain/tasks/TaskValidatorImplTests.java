package com.koszczi.userswithtasks.domain.tasks;

import com.koszczi.userswithtasks.domain.tasks.validation.TaskValidationConfig;
import com.koszczi.userswithtasks.domain.tasks.validation.TaskValidatorImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class TaskValidatorImplTests {
  TaskValidatorImpl validator;
  Task task;

  @BeforeEach
  public void setup() {
    TaskValidationConfig validationConfig = new TaskValidationConfig();
    validationConfig.setNameNullable(false);
    validationConfig.setNameMaxLength(5);
    validationConfig.setDescriptionNullable(true);
    validationConfig.setDescriptionMaxLength(10);
    validationConfig.setDateTimeNullable(false);

    validator = new TaskValidatorImpl(validationConfig);

    task = TestTaskFactory.buildTask(11L, 1L);
  }

  @Test
  public void givenNameNull_returnsFalse() {
    ReflectionTestUtils.setField(task, "name", null);
    Assertions.assertFalse(validator.isValid(task));
  }

  @Test
  public void givenDateTimeNull_returnsFalse() {
    ReflectionTestUtils.setField(task, "dateTime", null);
    Assertions.assertFalse(validator.isValid(task));
  }

  @Test
  public void givenNameTooLong_returnsFalse() {
    ReflectionTestUtils.setField(task, "name", "123456789");
    Assertions.assertFalse(validator.isValid(task));
  }

  @Test
  public void givenDescriptionTooLong_returnsFalse() {
    ReflectionTestUtils.setField(task, "description", "123456789123456789");
    Assertions.assertFalse(validator.isValid(task));
  }

  @Test
  public void givenEveryFieldIsValid_returnsTrue() {
    Assertions.assertTrue(validator.isValid(task));
  }

  @Test
  public void givenEveryFieldIsValidDescriptionNull_returnsTrue() {
    ReflectionTestUtils.setField(task, "description", null);
    Assertions.assertTrue(validator.isValid(task));
  }

}
