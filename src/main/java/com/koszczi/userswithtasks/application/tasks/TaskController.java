package com.koszczi.userswithtasks.application.tasks;

import com.koszczi.userswithtasks.domain.tasks.Task;
import com.koszczi.userswithtasks.domain.users.User;
import com.koszczi.userswithtasks.domain.tasks.TaskPersistenceService;
import com.koszczi.userswithtasks.domain.tasks.validation.TaskValidator;
import com.koszczi.userswithtasks.domain.users.UserPersistenceService;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This service contains controller methods for querying or modifying {@link Task} objects
 */
@Controller
@RequestMapping("/user/{userId}/task")
@AllArgsConstructor
public class TaskController {

  public static final String GENERIC_ERR_MSG = "operation failed due to some internal error";
  public static final String USER_NOT_FOUND_ERR_MSG = "user not found";
  private final TaskPersistenceService taskPersistenceService;
  private final UserPersistenceService userPersistenceService;
  private final TaskValidator taskValidator;

  /**
   * Provides all {@link Task} belonging to a {@link User}
   * @param userId user identifier
   * @return {@link ResponseEntity} of Http Response code "OK" holding the {@link Task} instances.
   * If the {@link User} specified by userId does not exist, the response code is "BAD REQUEST"
   */
  @GetMapping
  public ResponseEntity<?> findAllTasksForUser(@PathVariable long userId) {
    if (!userPersistenceService.findUserById(userId).isPresent())
      return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(USER_NOT_FOUND_ERR_MSG);
    return ResponseEntity.ok(taskPersistenceService.findAllTasksForUser(userId));
  }

  /**
   * Provides a specific{@link Task} belonging to a {@link User}
   * @param userId user identifier
   * @param id identifier of the task
   * @return /**
   *    * Updates a specific {@link User} instance
   *    * @param id identifier
   *    * @param user prepared {@link User} instance
   *    * @return {@link ResponseEntity} of Http Response code "OK" holding the found resource if found,
   *    *  or Http Response code "NOT FOUND" otherwise
   *    * {@link Task} instance if found.
   * If the {@link Task} specified by id does not exist, the response code is "NOT FOUND"
   * If the {@link User} specified by userId does not exist, the response code is "BAD REQUEST"
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getTask(@PathVariable long userId, @PathVariable long id) {
    if (!userPersistenceService.findUserById(userId).isPresent())
      return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(USER_NOT_FOUND_ERR_MSG);
    return taskPersistenceService.findTaskById(id)
        .filter(t -> t.getUserId() == userId)
        .map(t -> ResponseEntity.ok(t))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Updates a specific{@link Task} belonging to a {@link User}
   * @param userId user identifier
   * @param id identifier of the task
   * @param task prepared {@link Task} instance in Json format
   * @return {@link ResponseEntity} of Http Response code "OK"
   * holding the {@link Task} instance if found.
   * If the {@link Task} specified by id does not exist, the response code is "NOT FOUND"
   * If the provided {@link Task} is invalid, the response code is "UNPROCESSABLE ENTITY"
   * If the {@link User} specified by userId does not exist, the response code is "BAD REQUEST"
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> updateTask(@PathVariable long userId, @PathVariable long id, @RequestBody Task task) {
    if (!userPersistenceService.findUserById(userId).isPresent())
      return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(USER_NOT_FOUND_ERR_MSG);

    if (invalid(task)) return unProcessableEntityResponse();

    return taskPersistenceService.findTaskById(id)
        .filter(t -> t.getUserId() == userId)
        .flatMap(t -> taskPersistenceService.persistTask(t.merge(task), userId))
        .map(t -> ResponseEntity.ok(t))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Creates a new {@link Task} belonging to a {@link User}
   * @param userId user identifier
   * @param task prepared {@link Task} instance in Json format
   * @param uriBuilder uri builder to create url of the new resource
   * @return {@link ResponseEntity} of Http Response code "OK"
   * holding the {@link Task} instance if found.
   * If the {@link Task} specified by id does not exist, the response code is "NOT FOUND"
   * If the provided {@link Task} is invalid, the response code is "UNPROCESSABLE ENTITY"
   * If the {@link User} specified by userId does not exist, the response code is "BAD REQUEST"
   */
  @PostMapping
  public ResponseEntity<?> postTask(@PathVariable long userId, @RequestBody Task task,
      UriComponentsBuilder uriBuilder) {
    if (!userPersistenceService.findUserById(userId).isPresent())
      return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(USER_NOT_FOUND_ERR_MSG);

    if (invalid(task)) return unProcessableEntityResponse();

    Optional<Task> savedTask = taskPersistenceService.persistTask(task, userId);
    if (savedTask.isPresent())
      return createdResponse(savedTask.get(), uriBuilder);
    else
      return genericErrorResponse();
  }

  private boolean invalid(Task task) {
    return !taskValidator.isValid(task);
  }

  /**
   * Deletes a specific{@link Task} belonging to a {@link User}
   * @param userId user identifier
   * @param id identifier of the task
   * @return {@link ResponseEntity} of Http Response code "NO CONTENT"
   * If the {@link Task} specified by id does not exist, the response code is "NOT FOUND"
   * If the {@link User} specified by userId does not exist, the response code is "BAD REQUEST"
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<?>  deleteTask(@PathVariable long userId, @PathVariable long id) {
    if (!userPersistenceService.findUserById(userId).isPresent())
      return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(USER_NOT_FOUND_ERR_MSG);
    return taskPersistenceService.findTaskById(id)
        .filter(t -> t.getUserId() == userId)
        .map(t -> { taskPersistenceService.deleteTask(id); return  ResponseEntity.noContent().build(); })
        .orElse(ResponseEntity.notFound().build());
  }


  private ResponseEntity<?> unProcessableEntityResponse() {
    return ResponseEntity
        .unprocessableEntity()
        .body(taskValidator.help());
  }

  private ResponseEntity<?> createdResponse(Task task, UriComponentsBuilder uriBuilder) {
    UriComponents uriComponents = uriBuilder
        .path("user/{userId}/task/{id}")
        .buildAndExpand(task.getUserId(), task.getId());
    return ResponseEntity
        .created(uriComponents.toUri())
        .body(task);
  }

  private ResponseEntity<?> genericErrorResponse() {
    return ResponseEntity
        .internalServerError()
        .body(GENERIC_ERR_MSG);
  }

}
