package com.datagenerator.das.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field {

  private String columnName;
  private String dataType;
  private String length;
  private boolean isPrimaryKey;
}
