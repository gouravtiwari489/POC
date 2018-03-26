package com.osi.datagen.domain;

import java.io.Serializable;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Field implements Serializable {

  private static final long serialVersionUID = 1L;
  private String columnName;
  private String dataType;
  private String length;
  private List<MappingCategory> mappingCategeries;
  private String mappedCategory;
  private String mappedType;
  private String defaultValue;
  private String incrementValue;
}
