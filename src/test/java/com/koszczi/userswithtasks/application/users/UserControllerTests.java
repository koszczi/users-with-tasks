package com.koszczi.userswithtasks.application.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koszczi.userswithtasks.domain.users.TestUserFactory;
import com.koszczi.userswithtasks.domain.users.User;
import com.koszczi.userswithtasks.domain.users.UserPersistenceServiceRDBMS;
import com.koszczi.userswithtasks.domain.users.validation.UserValidator;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
public class UserControllerTests {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private UserValidator userValidator;

  @MockBean
  private UserPersistenceServiceRDBMS persistenceService;

  @Test
  public void testGetOk() throws Exception {
    long id = 1;
    User user = TestUserFactory.buildUser(id);
    when(persistenceService.findUserById(id)).thenReturn(Optional.of(user));
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/" + id))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    System.out.println(response.getContentAsString());
    assertEquals(user, (new ObjectMapper()).readValue(response.getContentAsString(), User.class));
  }

  @Test
  public void testGetNotFound() throws Exception {
    long id = 1;
    User user = TestUserFactory.buildUser(id);
    when(persistenceService.findUserById(id)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/" + 1))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void testGetBadRequestDueToInvalidId() throws Exception {
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/DUMMY"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPostCreated() throws Exception {
    String input = "{\"username\":\"jsmith\",\"first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    User user = (new ObjectMapper()).readValue(input, User.class);

    when(userValidator.isValid(user)).thenReturn(true);
    when(persistenceService.persistUser(user)).thenReturn(Optional.of(user));
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals(user, (new ObjectMapper()).readValue(response.getContentAsString(), User.class));
  }

  @Test
  public void testPostUnprocessableEntity() throws Exception {
    String input = "{\"username\":\"jsmith\",\"first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    User user = (new ObjectMapper()).readValue(input, User.class);

    when(userValidator.isValid(user)).thenReturn(false);
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void testPostInternalServerError() throws Exception {
    String input = "{\"username\":\"jsmith\",\"first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    User user = (new ObjectMapper()).readValue(input, User.class);

    when(userValidator.isValid(user)).thenReturn(true);
    when(persistenceService.persistUser(user)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    assertEquals(UserController.GENERIC_ERR_MSG, response.getContentAsString());
  }

  @Test
  public void testPostBadRequestDueToInvalidJson() throws Exception {
    String invalidInput = "{\"username\":\"jsmith\",first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/")
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPutOk() throws Exception {
    long id = 1;
    String input = "{\"username\":\"jsmith\",\"first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    User user = (new ObjectMapper()).readValue(input, User.class);

    when(userValidator.isValid(user)).thenReturn(true);
    when(persistenceService.findUserById(id)).thenReturn(Optional.of(user));
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + id)
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(user, (new ObjectMapper()).readValue(response.getContentAsString(), User.class));
  }

  @Test
  public void testPutNotFoound() throws Exception {
    long id = 1;
    String input = "{\"username\":\"jsmith\",\"first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    User user = (new ObjectMapper()).readValue(input, User.class);

    when(userValidator.isValid(user)).thenReturn(true);
    when(persistenceService.findUserById(id)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + id)
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void testPutUnprocessableEntity() throws Exception {
    long id = 1;
    String input = "{\"username\":\"jsmith\",\"first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    User user = (new ObjectMapper()).readValue(input, User.class);

    when(userValidator.isValid(user)).thenReturn(false);
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + 1)
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void testPutBadRequestDueToInvalidJson() throws Exception {
    long id = 1;
    String invalidInput = "{\"username\":\"jsmith\",first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + id)
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPutBadRequestDueToInvalidId() throws Exception {
    String invalidInput = "{\"username\":\"jsmith\",first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/DUMMY")
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }
}
