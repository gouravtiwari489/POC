package com.datagenerator.das.domain;

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
