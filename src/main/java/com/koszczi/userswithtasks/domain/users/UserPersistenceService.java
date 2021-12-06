package com.koszczi.userswithtasks.domain.users;

import java.util.List;
import java.util.Optional;

/**
 * This service and its implementation(s) are responsible for any task regarding to reading and writing {@link User}
 * entities from/into a persistent storage
 */

public interface UserPersistenceService {

  /**
   * Checks whether if a {@link User} object with given ID is present in the persistent storage or not
   * @param id ID value
   * @return true if the persistent storage contains an entity with the given Id, false otherwise
   */
  boolean userExistsById(long id);

  /**
   * Finds and returns a {@link User} entity from the persistent storage based on an Id
   * @param id Id value
   * @return {@link User} entity wrapped into {@link Optional} if found, {@code Optional.empty()} otherwise
   */
  Optional<User> findUserById(long id);

  /**
   * Finds all {@link User} entities
   * @return list of {@link User} entities
   */
  List<User> findAllUsers();

  /**
   * Attempts to persist a given {@link User} entity into the persistent storage. If an exception occurs during
   * the operation, it is caught and {@code Optional.empty()} is returned
   * @param userToPersist {@link User} entity to persist
   * @return persisted {@link User} entity if the operation was successful, {@code Optional.empty()} otherwise
   */
  Optional<User> persistUser(User userToPersist);

}
