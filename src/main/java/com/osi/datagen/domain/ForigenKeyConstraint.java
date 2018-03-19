package com.osi.datagen.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForigenKeyConstraint {
  private String constraintName;
  private String referenceTable;
  private String referenceColumn;
  private String keyName;
  
}
