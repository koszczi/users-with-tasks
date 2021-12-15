package com.koszczi.userswithtasks.domain.tasks;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of {@link Task} entity providing methods for CRUD operations
 */
public interface TaskRepository extends CrudRepository<Task, Long> {

  List<Task> findAllByUserId(long userId);

  List<Task> findAllByStatus(Task.Status status);
}
