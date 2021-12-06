package com.koszczi.userswithtasks.domain.tasks;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

  List<Task> findAllByUserId(long userId);
}
