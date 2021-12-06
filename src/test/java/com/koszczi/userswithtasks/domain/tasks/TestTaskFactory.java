package com.koszczi.userswithtasks.domain.tasks;

import java.time.LocalDate;
import java.util.Random;
import org.springframework.test.util.ReflectionTestUtils;

public class TestTaskFactory {

  public static Task buildTask(Long userId, Long id) {
    Task task = Task.builder()
        .name(generateString(5))
        .description(generateString(10))
        .userId(userId)
        .dateTime(LocalDate.of(2020, 11, 11))
        .build();

    if (id != null) {
      ReflectionTestUtils.setField(task, "id", 1L);
    }

    return task;
  }

  private static String generateString(int length) {
    int leftLimit = 97;
    int rightLimit = 122;
    int targetStringLength = length;
    Random random = new Random();

    String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    return generatedString;
  }
}
