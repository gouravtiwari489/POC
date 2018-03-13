package com.osi.dgen.domain;

import java.util.List;
import lombok.Data;
import lombok.Generated;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class User {

  @Id @Generated private String id;
  private String username;
  private String password;
  private List<Role> roles;

  public User() {}

  public User(String username, String password, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
}
