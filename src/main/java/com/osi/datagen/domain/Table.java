package com.osi.datagen.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Table implements Serializable {

  private static final long serialVersionUID = 1L;
  private String tableName;
  private List<Field> fields;
  List<Constraint> constraints;
  List<ForigenKeyConstraint> forigenKeys;
  List<CheckConstraint> checkConstraints;

  public List<String> getFieldsNames() {

    return fields.stream().map(f -> f.getColumnName()).collect(Collectors.toList());

  }

  public Constraint getPrimaryKey() {

    Optional<Constraint> constraint = constraints.stream()
        .filter(cons -> cons.getConstraintType().equalsIgnoreCase("PRIMARY KEY")).findFirst();
    return constraint.get();
  }


  public boolean isSelfDependencyFound() {
    long count =
        forigenKeys.stream().filter(fk -> fk.getReferenceTable().equals(tableName)).count();
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }
  
 

}
