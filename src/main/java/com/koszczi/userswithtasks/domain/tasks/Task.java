package com.koszczi.userswithtasks.domain.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "TASK",
    indexes = {@Index(name = "USERID", columnList = "USER_ID"),
        @Index(name = "STATUS", columnList = "STATUS")})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@Getter
public class Task {

  public enum Status { PENDING, DONE; }

  @Id
  @Column(name = "ID")
  @GeneratedValue
  private Long id;

  @Column(name = "NAME")
  @JsonProperty("name")
  @EqualsAndHashCode.Include
  private String name;

  @Column(name = "DESCRIPTION")
  @JsonProperty("description")
  private String description;

  @Column(name = "DATE_TIME")
  @JsonProperty("date_time")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateTime;

  @Column(name = "USER_ID")
  @Setter(AccessLevel.PACKAGE)
  @EqualsAndHashCode.Include
  private long userId;

  @Column(name = "STATUS")
  private Status status = Status.PENDING;

  public static TaskBuilder builder() {
    return new TaskBuilder();
  }

  public Task merge(Task other) {
    this.name = other.name;
    this.description = other.description;
    this.status = other.status;
    return this;
  }

  public Task completed() {
    this.status = Status.DONE;
    return this;
  }

  public static class TaskBuilder {

    private String name;
    private String description;
    private LocalDateTime dateTime;
    private long userId;

    TaskBuilder() {
    }

    public TaskBuilder name(String name) {
      this.name = name;
      return this;
    }

    public TaskBuilder description(String description) {
      this.description = description;
      return this;
    }

    public TaskBuilder dateTime(LocalDateTime dateTime) {
      this.dateTime = dateTime;
      return this;
    }

    public TaskBuilder userId(long userId) {
      this.userId = userId;
      return this;
    }

    public Task build() {
      return new Task(this);
    }
  }

  private Task(TaskBuilder builder) {
    this.name = builder.name;
    this.description = builder.description;
    this.dateTime = builder.dateTime;
    this.userId = builder.userId;
    this.status = Status.PENDING;
  }
}
