package com.osi.dgen.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Field implements Serializable{

  private static final long serialVersionUID = 1L;
  private String columnName;
  private String dataType;
  private String length;
  private boolean isPrimaryKey;
  private boolean isUniqueKey;
  private boolean isForeignKey;
  private String  referenceTable;
  private String  referenceColumn;
  private String defaultValue;
  private String mappingCategory;
  private String mappingType;
  
  
}
