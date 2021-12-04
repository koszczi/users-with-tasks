package com.koszczi.userswithtasks.domain.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserPersistenceServiceRDBMSTests {

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private UserPersistenceServiceRDBMS service;

  @Test
  public void userExistsByIdShouldReturnTheSameAsTheRepo() {
    Long id = Long.valueOf(1L);
    when(userRepository.existsById(id)).thenReturn(true);
    assertTrue(service.userExistsById(id), "userExistsById should return TRUE as it is returned from repo too");
    when(userRepository.existsById(id)).thenReturn(false);
    assertFalse(service.userExistsById(id), "userExistsById should return FALSE as it is returned from repo too");
  }

  @Test
  public void findUserByIdShouldReturnTheSameAsTheRepo() {
    Long id = Long.valueOf(1L);
    Optional<User> user = Optional.of(TestUserFactory.buildUser(id));
    when(userRepository.findById(id)).thenReturn(user);
    assertEquals(user, service.findUserById(id));
    when(userRepository.findById(id)).thenReturn(Optional.empty());
    assertFalse(service.findUserById(id).isPresent());
  }

  @Test
  public void givenUserPersistSuccessful_userIsReturned() {
    User userFromRepo = TestUserFactory.buildUser(1L);
    User input = TestUserFactory.buildUser(2L);
    when(userRepository.save(input)).thenReturn(userFromRepo);
    Optional<User> result = service.persistUser(input);
    assertTrue(result.isPresent());
    assertEquals(userFromRepo, result.get());
  }

  @Test
  public void givenUserPersistFails_emptyIsReturned() {
    User input = TestUserFactory.buildUser(2L);
    when(userRepository.save(input)).thenThrow(new RuntimeException("db problem"));
    Optional<User> result = service.persistUser(input);
    assertFalse(result.isPresent());
  }
}
