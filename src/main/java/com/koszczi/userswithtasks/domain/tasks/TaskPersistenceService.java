package com.koszczi.userswithtasks.domain.tasks;

import com.koszczi.userswithtasks.domain.tasks.Task;
import com.koszczi.userswithtasks.domain.users.User;
import java.util.List;
import java.util.Optional;

/**
 * This service and its implementation(s) are responsible for any task regarding to reading and writing {@link Task}
 * entities from/into a persistent storage
 */
public interface TaskPersistenceService {

  /**
   * Checks whether if a {@link Task} object with given ID is present in the persistent storage or not
   * @param id ID value
   * @return true if the persistent storage contains an entity with the given Id, false otherwise
   */
  boolean taskExistsById(long id);

  /**
   * Finds and returns a {@link Task} entity from the persistent storage based on an Id
   * @param id Id value
   * @return {@link Task} entity wrapped into {@link Optional} if found, {@code Optional.empty()} otherwise
   */
  Optional<Task> findTaskById(long id);

  /**
   * Finds all {@link Task} entities for a given {@link User}
   * @param userId user identifier
   * @return list of {@link Task} entities
   */
  List<Task> findAllTasksForUser(long userId);

  /**
   * Attempts to persist a given {@link Task} entity into the persistent storage. If an exception occurs during
   * the operation, it is caught and {@code Optional.empty()} is returned
   * @param TaskToPersist {@link Task} entity to persist
   * @param userId value for userId attribute
   * @return persisted {@link Task} entity if the operation was successful, {@code Optional.empty()} otherwise
   */
  Optional<Task> persistTask(Task TaskToPersist, long userId);

  /**
   * Deletes a {@link Task} entry with a given id
   * @param id id value
   */
  void deleteTask(long id);

}

