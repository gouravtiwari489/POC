package com.osi.datagen.domain;

import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Field implements Serializable{

  private static final long serialVersionUID = 1L;
  private String columnName;
  private String dataType;
  private String length;
  private List<MappedCategory> mappedCategory;
  private String defaultValue;
  
}
