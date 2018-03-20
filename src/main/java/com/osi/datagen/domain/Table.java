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
  private List<Constraint> constraints;
  private List<ForigenKeyConstraint> forigenKeys;
  private List<CheckConstraint> checkConstraints;

  public List<String> getFieldsNames() {

    return fields.stream().map(f -> f.getColumnName()).collect(Collectors.toList());

  }

  public Constraint getPrimaryKey() {

    Optional<Constraint> constraint = constraints.stream()
        .filter(cons -> cons.getConstraintType().equalsIgnoreCase("PRIMARY KEY")).findFirst();
    return constraint.get();
  }
  
  
  public List<Field> getPrimaryKeyFields() {

    Constraint constraint=getPrimaryKey();
    return constraint.getColumns().stream().map(c->getField(c)).collect(Collectors.toList());
  }
  
  public List<Constraint> getUniqueKeys() {

   return constraints.stream()
        .filter(cons -> cons.getConstraintType().equalsIgnoreCase("UNIQUE KEY")).collect(Collectors.toList());
  }

 public List<List<Field>> getUniqueKeyFields() {

   List<Constraint> constraints=getUniqueKeys();
   
   return constraints.stream().map(constr->getFields(constr.getColumns())).collect(Collectors.toList());
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

  public Field getField(String columnName) {
    return fields.stream()
        .filter(f->f.getColumnName().equalsIgnoreCase(columnName)).findFirst().get();
  }
  
  public List<Field> getFields(List<String> columnNames) {
    return fields.stream()
        .filter(f->columnNames.contains(f.getColumnName())).collect(Collectors.toList());
  }
 

}
