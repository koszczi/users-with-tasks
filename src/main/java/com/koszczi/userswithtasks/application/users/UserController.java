package com.koszczi.userswithtasks.application.users;

import com.koszczi.userswithtasks.domain.tasks.Task;
import com.koszczi.userswithtasks.domain.users.User;
import com.koszczi.userswithtasks.domain.users.UserPersistenceService;
import com.koszczi.userswithtasks.domain.users.validation.UserValidator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * This service contains controller methods for querying or modifying {@link User} objects
 */
@Controller
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

  public static final String GENERIC_ERR_MSG = "operation failed due to some internal error";
  private final UserPersistenceService userPersistenceService;
  private final UserValidator userValidator;

  /**
   * Provides all {@link User} instances
   * @return {@link ResponseEntity} of Http Response code "OK" holding the found resources
   */
  @GetMapping
  public ResponseEntity<List<User>> findAllUsers() {
    return ResponseEntity.ok(userPersistenceService.findAllUsers());
  }

  /**
   * Provides a specific {@link User} instance
   * @param id identifier
   * @return {@link ResponseEntity} of Http Response code "OK" holding the found resource if found,
   *  or Http Response code "NOT FOUND" otherwise
   */
  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable long id) {
    return userPersistenceService.findUserById(id)
        .map(u -> ResponseEntity.ok(u))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Updates a specific {@link User} instance
   * @param id identifier
   * @param user prepared {@link User} instance
   * @return {@link ResponseEntity} of Http Response code "OK" holding the found resource if found,
   *  or Http Response code "NOT FOUND" otherwise
   */
  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User user) {

    if (invalid(user)) return unProcessableEntityResponse();

    return userPersistenceService.findUserById(id)
        .flatMap(u -> userPersistenceService.persistUser(u.merge(user)))
        .map(u -> ResponseEntity.ok(u))
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * Creates a specific {@link User} instance
   * @param user prepared {@link User} instance
   * @param uriBuilder uri builder to create url of the new resource
   * @return {@link ResponseEntity} of Http Response code "OK" holding the found resource if found,
   *  or Http Response code "NOT FOUND" otherwise
   */
  @PostMapping
  public ResponseEntity<?> postUser(@RequestBody User user,
      UriComponentsBuilder uriBuilder) {

    if (invalid(user)) return unProcessableEntityResponse();

    Optional<User> savedUser = userPersistenceService.persistUser(user);
    if (savedUser.isPresent())
      return createdResponse(savedUser.get(), uriBuilder);
    else
      return genericErrorResponse();
  }

  private boolean invalid(User user) {
    return !userValidator.isValid(user);
  }

  private ResponseEntity<?> unProcessableEntityResponse() {
    return ResponseEntity
        .unprocessableEntity()
        .body(userValidator.help());
  }

  private ResponseEntity<?> createdResponse(User user, UriComponentsBuilder uriBuilder) {
    UriComponents uriComponents = uriBuilder
        .path("user/{id}")
        .buildAndExpand(user.getId());
    return ResponseEntity
        .created(uriComponents.toUri())
        .body(user);
  }

  private ResponseEntity<?> genericErrorResponse() {
    return ResponseEntity
        .internalServerError()
        .body(GENERIC_ERR_MSG);
  }

}
