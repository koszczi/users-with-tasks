package com.koszczi.userswithtasks.domain.users;

import com.koszczi.userswithtasks.domain.tasks.Task;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository of {@link Task} entity providing methods for CRUD operations
 */
public interface UserRepository extends CrudRepository<User, Long> {

  List<User> findAll();
}
