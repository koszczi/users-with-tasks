package com.koszczi.userswithtasks.application.tasks;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koszczi.userswithtasks.domain.tasks.Task;
import com.koszczi.userswithtasks.domain.tasks.TaskPersistenceServiceRDBMS;
import com.koszczi.userswithtasks.domain.tasks.TestTaskFactory;
import com.koszczi.userswithtasks.domain.tasks.validation.TaskValidator;
import com.koszczi.userswithtasks.domain.users.TestUserFactory;
import com.koszczi.userswithtasks.domain.users.User;
import com.koszczi.userswithtasks.domain.users.UserPersistenceServiceRDBMS;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(TaskController.class)
public class TaksControllerTests {

  @Autowired
  private MockMvc mvc;

  @MockBean
  private TaskValidator taskValidator;

  @MockBean
  private TaskPersistenceServiceRDBMS persistenceService;

  @MockBean
  private UserPersistenceServiceRDBMS userPersistenceService;

  private long userId = 11;
  private User user = TestUserFactory.buildUser(userId);

  @Test
  public void testGetOk() throws Exception {
    long id = 1;
    
    Task task = TestTaskFactory.buildTask(userId, id);
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(persistenceService.findTaskById(id)).thenReturn(Optional.of(task));
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/" + userId + "/task/" + id))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    System.out.println(response.getContentAsString());
    assertEquals(task, (new ObjectMapper()).readValue(response.getContentAsString(), Task.class));
  }

  @Test
  public void testGetNotFound() throws Exception {
    long id = 1;
    
    Task Task = TestTaskFactory.buildTask(userId, id);
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(persistenceService.findTaskById(id)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/" + userId + "/task/" + 1))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void testGetUserNotFound() throws Exception {
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/" + userId + "/task/" + 1))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testGetBadRequestDueToInvalidId() throws Exception {
    
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders.get("/user/" + userId + "/task/DUMMY"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPostCreated() throws Exception {
    
    String input = "{\"name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    Task task = (new ObjectMapper()).readValue(input, Task.class);

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(taskValidator.isValid(task)).thenReturn(true);
    when(persistenceService.persistTask(task, userId)).thenReturn(Optional.of(task));
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/" + userId + "/task")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    assertEquals(task, (new ObjectMapper()).readValue(response.getContentAsString(), Task.class));
  }

  @Test
  public void testPostUserNotFound() throws Exception {
    String input = "{\"name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/" + userId + "/task")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }
  @Test
  public void testPostUnprocessableEntity() throws Exception {
    
    String input = "{\"name\":null,\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    Task Task = (new ObjectMapper()).readValue(input, Task.class);

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(taskValidator.isValid(Task)).thenReturn(false);
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/" + userId + "/task")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void testPostInternalServerError() throws Exception {
    
    String input = "{\"name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    Task task = (new ObjectMapper()).readValue(input, Task.class);

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(taskValidator.isValid(task)).thenReturn(true);
    when(persistenceService.persistTask(task, userId)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/" + userId + "/task")
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
    assertEquals(TaskController.GENERIC_ERR_MSG, response.getContentAsString());
  }

  @Test
  public void testPostBadRequestDueToInvalidJson() throws Exception {
    
    String invalidInput = "{name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .post("/user/" + userId + "/task")
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPutOk() throws Exception {
    long id = 1;
    
    String input = "{\"name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    Task inputTask = (new ObjectMapper()).readValue(input, Task.class);
    Task taskFromDb = (new ObjectMapper()).readValue(input, Task.class);
    ReflectionTestUtils.setField(taskFromDb, "userId", userId);

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(taskValidator.isValid(inputTask)).thenReturn(true);
    when(persistenceService.findTaskById(id)).thenReturn(Optional.of(taskFromDb));
    when(persistenceService.persistTask(taskFromDb, userId)).thenReturn(Optional.of(taskFromDb));
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + userId + "/task/" + id)
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals(taskFromDb, (new ObjectMapper()).readValue(response.getContentAsString(), Task.class));
  }

  @Test
  public void testPutNotFound() throws Exception {
    long id = 1;
    
    String input = "{\"name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    Task task = (new ObjectMapper()).readValue(input, Task.class);

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(taskValidator.isValid(task)).thenReturn(true);
    when(persistenceService.findTaskById(id)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + userId + "/task/" + id)
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void testPutUnprocessableEntity() throws Exception {
    long id = 1;
    
    String input = "{\"name\":\"My task\",\"description\" : \"New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    Task task = (new ObjectMapper()).readValue(input, Task.class);

    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(taskValidator.isValid(task)).thenReturn(false);
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + userId + "/task/" + 1)
            .content(input)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), response.getStatus());
  }

  @Test
  public void testPutBadRequestDueToInvalidJson() throws Exception {
    long id = 1;
    
    String invalidInput = "{\"name\":\"My task\",\"description\" : New Description of task\", \"date_time\" : \"2016-05-26 14:25:00\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + userId + "/task/" + id)
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPutBadRequestDueToInvalidId() throws Exception {
    
    String invalidInput = "{\"Taskname\":\"jsmith\",first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + userId + "/task/DUMMY")
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testPutUserNotFound() throws Exception {
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.empty());
    String invalidInput = "{\"Taskname\":\"jsmith\",first_name\" : \"Johnny\", \"last_name\" : \"Smith\"}";
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .put("/user/" + userId + "/task/DUMMY")
            .content(invalidInput)
            .contentType("application/json"))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }

  @Test
  public void testDeleteOk() throws Exception {
    long id = 1;
    Task taskFromDb = TestTaskFactory.buildTask(userId, id);
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(persistenceService.findTaskById(id)).thenReturn(Optional.of(taskFromDb));
    doNothing().when(persistenceService).deleteTask(id);
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .delete("/user/" + userId + "/task/" + id))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
  }

  @Test
  public void testDeleteNotFound() throws Exception {
    long id = 1;
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.of(user));
    when(persistenceService.findTaskById(id)).thenReturn(Optional.empty());
    doNothing().when(persistenceService).deleteTask(id);
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .delete("/user/" + userId + "/task/" + id))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
  }

  @Test
  public void testDeleteUserNotFound() throws Exception {
    long id = 1;
    when(userPersistenceService.findUserById(userId)).thenReturn(Optional.empty());
    MockHttpServletResponse response = mvc
        .perform(MockMvcRequestBuilders
            .delete("/user/" + userId + "/task/" + id))
        .andReturn()
        .getResponse();
    assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
  }
}

