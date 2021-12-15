package com.koszczi.userswithtasks.application.tasks;

import com.koszczi.userswithtasks.domain.tasks.Task.Status;
import com.koszczi.userswithtasks.domain.tasks.TaskRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Schedule background job to take care of Tasks with PENDING status for too long
 */
@Service
@AllArgsConstructor
public class ScheduledJob {

  private final TaskRepository taskRepository;

  @Scheduled(fixedDelayString = "${scheduled.job.frequency.ms}")
  public void completeTasks() {

    taskRepository.findAllByStatus(Status.PENDING)
        .stream()
        .filter(t -> LocalDateTime.now().isAfter(t.getDateTime()))
        .forEach(t -> taskRepository.save(t.completed()));
  }
}
