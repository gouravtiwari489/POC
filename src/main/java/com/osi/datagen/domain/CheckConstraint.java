package com.osi.datagen.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class CheckConstraint {
  private String constraintName;
  private String value;
}
