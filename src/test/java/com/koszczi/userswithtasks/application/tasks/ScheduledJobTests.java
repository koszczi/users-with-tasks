package com.koszczi.userswithtasks.application.tasks;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.koszczi.userswithtasks.domain.tasks.Task;
import com.koszczi.userswithtasks.domain.tasks.Task.Status;
import com.koszczi.userswithtasks.domain.tasks.TaskRepository;
import com.koszczi.userswithtasks.domain.tasks.TestTaskFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ScheduledJobTests {

  @Mock
  TaskRepository taskRepository;
  @InjectMocks
  ScheduledJob scheduledJob;

  @Test
  public void testNecessarryTasksUpdated() {
    Task overdue = TestTaskFactory.buildTask(1L, 1L);
    ReflectionTestUtils.setField(overdue, "dateTime", LocalDateTime.now().minusDays(1));
    Task notOverdue = TestTaskFactory.buildTask(1L, 2L);
    ReflectionTestUtils.setField(notOverdue, "dateTime", LocalDateTime.now().plusDays(1));
    List<Task> tasks = new ArrayList<>();
    tasks.add(overdue);
    tasks.add(notOverdue);

    when(taskRepository.findAllByStatus(Status.PENDING)).thenReturn(tasks);
    scheduledJob.completeTasks();
    verify(taskRepository, times(1)).save(overdue);
    verify(taskRepository, never()).save(notOverdue);
  }
}
