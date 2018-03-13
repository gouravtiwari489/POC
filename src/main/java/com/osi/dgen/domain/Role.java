package com.osi.dgen.domain;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Role {

  private String name;

  public Role() {}

  public Role(String name) {
    this.name = name;
  }
}
