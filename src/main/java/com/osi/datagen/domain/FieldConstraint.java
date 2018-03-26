package com.osi.datagen.domain;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldConstraint implements Serializable {
  private static final long serialVersionUID = 1L;
  private String constraintName;
  private String constraintValue;
}
