package com.osi.datagen.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ForigenKeyConstraint {
  private String constraintName;
  private String referenceTable;
  private String referenceColumn;
  private String keyName;
}
