package com.osi.datagen.domain;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Constraint {
  private String constraintName;
  private String ConstraintType;
  private List<String> columns;

}
