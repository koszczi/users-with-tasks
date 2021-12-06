package com.koszczi.userswithtasks.domain.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "USER")
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Getter
public class Task {

  @Id
  @Column(name = "ID")
  @GeneratedValue
  private Long id;

  @Column(name = "NAME")
  @JsonProperty("name")
  private String name;

  @Column(name = "DESCRIPTION")
  @JsonProperty("description")
  private String description;

  @Column(name = "DATE_TIME")
  @JsonProperty("dateTime")
  private LocalDate dateTime;

  @Column(name = "USER_ID")
  private long userId;

  public static TaskBuilder builder() {
    return new TaskBuilder();
  }

  public Task merge(Task other) {
    this.name = other.name;
    this.description = other.description;
    return this;
  }

  public static class TaskBuilder {

    private String name;
    private String description;
    private LocalDate dateTime;
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

    public TaskBuilder dateTime(LocalDate dateTime) {
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
  }
}
