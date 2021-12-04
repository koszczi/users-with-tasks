package com.koszczi.userswithtasks.domain.users;

import java.util.Random;
import org.springframework.test.util.ReflectionTestUtils;

public class TestUserFactory {

  public static User buildUser(Long id) {
    User user = User.builder()
        .userName(generateString())
        .firstName(generateString())
        .lastName(generateString())
        .build();

    if (id != null) {
      ReflectionTestUtils.setField(user, "id", 1L);
    }

    return user;
  }

  private static String generateString() {
    int leftLimit = 97;
    int rightLimit = 122;
    int targetStringLength = 10;
    Random random = new Random();

    String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    return generatedString;
  }
}
