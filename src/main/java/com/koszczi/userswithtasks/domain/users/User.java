package com.koszczi.userswithtasks.domain.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entity class representing a User
 */

@Entity
@Table(name = "USER")
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Getter
public class User {

  @Id
  @Column(name = "ID")
  @GeneratedValue
  private Long id;

  @Column(name = "USER_NAME")
  @JsonProperty("username")
  private String userName;

  @Column(name = "LAST_NAME")
  @JsonProperty("last_name")
  private String lastName;

  @Column(name = "FIRST_NAME")
  @JsonProperty("first_name")
  private String firstName;

  /**
   * Merges the current user with another one
   * @param other the other user
   * @return the current user
   */
  public User merge(User other) {
    this.userName = other.userName;
    this.firstName = other.firstName;
    this.lastName = other.lastName;
    return this;
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

  public static class UserBuilder {

    private String userName;
    private String lastName;
    private String firstName;

    UserBuilder() {
    }

    public UserBuilder userName(String userName) {
      this.userName = userName;
      return this;
    }

    public UserBuilder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public UserBuilder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }

  private User(UserBuilder builder) {
    this.userName = builder.userName;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
  }

}
