package com.osi.datagen.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Constraint {
  private String constraintName;
  private String ConstraintType;
  private List<String> columns;

}
