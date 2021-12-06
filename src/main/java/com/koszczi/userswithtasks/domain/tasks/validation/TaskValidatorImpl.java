package com.koszczi.userswithtasks.domain.tasks.validation;

import com.koszczi.userswithtasks.domain.tasks.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TaskValidatorImpl implements TaskValidator {

  private static final String DUMMY_STRING = "X";

  private final TaskValidationConfig validationConfig;

  @Override
  public boolean isValid(Task Task) {
    return nullabilitiesAreOkFor(Task) && lengthsAreOkFor(Task);
  }

  private boolean lengthsAreOkFor(Task task) {
    return (task.getName() != null ? task.getName() : DUMMY_STRING).length() <= validationConfig.getNameMaxLength()
        && (task.getDescription() != null ? task.getDescription() : DUMMY_STRING).length() <= validationConfig.getDescriptionMaxLength();
  }

  private boolean nullabilitiesAreOkFor(Task task) {
    return nullabilityIsOk(task.getName(), validationConfig.isNameNullable())
        && nullabilityIsOk(task.getDescription(), validationConfig.isDescriptionNullable())
        && nullabilityIsOk(task.getDateTime(), validationConfig.isDateTimeNullable());
  }

  private <T extends Object> boolean nullabilityIsOk(T field, boolean nullability) {
    if (nullability != true) return field != null;
    return true;
  }

  public String help() {
    return new StringBuffer()
        .append("Name: ")
        .append("\n")
        .append("nullable: ").append(validationConfig.isNameNullable())
        .append("\n")
        .append("max length: ").append(validationConfig.getNameMaxLength())
        .append("\n\n")
        .append("Description: ")
        .append("\n")
        .append("nullable: ").append(validationConfig.isDescriptionNullable())
        .append("\n")
        .append("max length: ").append(validationConfig.getDescriptionMaxLength())
        .append("\n\n")
        .append("DateTime: ")
        .append("\n")
        .append("nullable: ").append(validationConfig.isDateTimeNullable())
        .toString();

  }
}

