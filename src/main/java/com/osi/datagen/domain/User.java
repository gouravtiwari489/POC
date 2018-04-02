package com.osi.datagen.domain;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {

  @Id @Generated private String id;
  @Indexed(unique=true)
  private String username;
  private String password;
  private List<Role> roles;
  @Indexed(unique=true)
	String emailId;
  @NotNull private String fullName;
  @NotNull private String gender;

  public User() {}

  public User(String username, String password, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
}
