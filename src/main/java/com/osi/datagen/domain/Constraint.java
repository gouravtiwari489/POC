package com.osi.datagen.domain;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Constraint {
  private String constraintName;
  private String ConstraintType;
  private List<String> columns;

}
