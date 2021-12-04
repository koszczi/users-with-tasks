package com.koszczi.userswithtasks.domain.users;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * This class implements the {@link UserPersistenceService} ineterface. It uses JPA-managed RDBMS as persistent storage
 */
@Service
@Slf4j
@AllArgsConstructor
public class UserPersistenceServiceRDBMS implements UserPersistenceService {
  private final UserRepository userRepository;

  @Override
  public boolean userExistsById(long id) {
    return userRepository.existsById(id);
  }

  @Override
  public Optional<User> findUserById(long id) {
    return userRepository.findById(id);
  }

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Optional<User> persistUser(User userToPersist) {
    try {
      return Optional.of(userRepository.save(userToPersist));
    } catch (Exception e) {
      log.error("Error persisting user", e);
      return Optional.empty();
    }
  }
}
