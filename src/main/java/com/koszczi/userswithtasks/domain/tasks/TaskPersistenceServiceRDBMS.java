package com.koszczi.userswithtasks.domain.tasks;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class implements the {@link TaskPersistenceService} ineterface. It uses JPA-managed RDBMS as persistent storage
 */
@Service
@Slf4j
@AllArgsConstructor
public class TaskPersistenceServiceRDBMS implements TaskPersistenceService {
  private final TaskRepository taskRepository;

  @Override
  public boolean taskExistsById(long id) {
    return taskRepository.existsById(id);
  }

  @Override
  public Optional<Task> findTaskById(long id) {
    return taskRepository.findById(id);
  }

  @Override
  @Transactional(propagation = Propagation.NOT_SUPPORTED)
  public Optional<Task> persistTask(Task taskToPersist, long userId) {
    try {
      taskToPersist.setUserId(userId);
      return Optional.of(taskRepository.save(taskToPersist));
    } catch (Exception e) {
      log.error("Error persisting Task", e);
      return Optional.empty();
    }
  }

  @Override
  public List<Task> findAllTasksForUser(long userId) { return taskRepository.findAllByUserId(userId);  }

  @Override
  public void deleteTask(long id) { taskRepository.deleteById(id);  }
}
