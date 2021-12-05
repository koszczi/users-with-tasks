package com.koszczi.userswithtasks.application.users;

import com.koszczi.userswithtasks.domain.users.User;
import com.koszczi.userswithtasks.domain.users.UserPersistenceService;
import com.koszczi.userswithtasks.domain.users.validation.UserValidator;
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

@Controller
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

  public static final String GENERIC_ERR_MSG = "operation failed due to some internal error";
  private final UserPersistenceService userPersistenceService;
  private final UserValidator userValidator;

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable long id) {
    return userPersistenceService.findUserById(id)
        .map(u -> ResponseEntity.ok(u))
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User user) {

    if (invalid(user)) return unProcessableEntityResponse();

    return userPersistenceService.findUserById(id)
        .map(u -> ResponseEntity.ok(u.merge(user)))
        .orElse(ResponseEntity.notFound().build());
  }

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
