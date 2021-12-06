package com.koszczi.userswithtasks.domain.tasks;

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
public class TaskPersistenceServiceRDBMSTests {

  @Mock
  private TaskRepository taskRepository;
  @InjectMocks
  private TaskPersistenceServiceRDBMS service;

  @Test
  public void TaskExistsByIdShouldReturnTheSameAsTheRepo() {
    Long id = Long.valueOf(1L);
    when(taskRepository.existsById(id)).thenReturn(true);
    assertTrue(service.taskExistsById(id), "TaskExistsById should return TRUE as it is returned from repo too");
    when(taskRepository.existsById(id)).thenReturn(false);
    assertFalse(service.taskExistsById(id), "TaskExistsById should return FALSE as it is returned from repo too");
  }

  @Test
  public void findTaskByIdShouldReturnTheSameAsTheRepo() {
    Long id = Long.valueOf(1L);
    Long userId = Long.valueOf(11L);
    Optional<Task> Task = Optional.of(TestTaskFactory.buildTask(userId, id));
    when(taskRepository.findById(id)).thenReturn(Task);
    assertEquals(Task, service.findTaskById(id));
    when(taskRepository.findById(id)).thenReturn(Optional.empty());
    assertFalse(service.findTaskById(id).isPresent());
  }

  @Test
  public void givenTaskPersistSuccessful_TaskIsReturned() {
    Long userId = Long.valueOf(11L);
    Task taskFromRepo = TestTaskFactory.buildTask(userId, 1L);
    Task input = TestTaskFactory.buildTask(userId,2L);
    when(taskRepository.save(input)).thenReturn(taskFromRepo);
    Optional<Task> result = service.persistTask(input, userId);
    assertTrue(result.isPresent());
    assertEquals(taskFromRepo, result.get());
    assertEquals(userId, taskFromRepo.getUserId());
  }

  @Test
  public void givenTaskPersistFails_emptyIsReturned() {
    Long userId = Long.valueOf(11L);
    Task input = TestTaskFactory.buildTask(userId, 2L);
    when(taskRepository.save(input)).thenThrow(new RuntimeException("db problem"));
    Optional<Task> result = service.persistTask(input, userId);
    assertFalse(result.isPresent());
  }
}
