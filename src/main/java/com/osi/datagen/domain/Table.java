package com.osi.datagen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Table {

  private String tableName;
  private List<Field> fields;
  private List<Constraint> constraints;
  private List<ForigenKeyConstraint> forigenKeys;
  private List<CheckConstraint> checkConstraints;

  @JsonIgnore
  public List<String> getFieldsNames() {

    return fields.stream().map(f -> f.getColumnName()).collect(Collectors.toList());
  }

  @JsonIgnore
  public Constraint getPrimaryKey() {
    if (constraints != null && !constraints.isEmpty()) {
      Optional<Constraint> constraint =
          constraints
              .stream()
              .filter(
                  cons ->
                      cons.getConstraintType() != null
                          && cons.getConstraintType().equalsIgnoreCase("PRIMARY KEY"))
              .findFirst();
      return constraint.isPresent() ? constraint.get() : null;
    }
    return null;
  }

  @JsonIgnore
  public List<Field> getPrimaryKeyFields() {

    Constraint constraint = getPrimaryKey();
    if (constraint != null) {
      return constraint.getColumns().stream().map(c -> getField(c)).collect(Collectors.toList());
    } else {
      return null;
    }
  }

  @JsonIgnore
  public List<Constraint> getUniqueKeys() {

    return constraints
        .stream()
        .filter(cons -> cons != null && cons.getConstraintType().equalsIgnoreCase("UNIQUE KEY"))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public List<List<Field>> getUniqueKeyFields() {

    List<Constraint> constraints = getUniqueKeys();

    return constraints
        .stream()
        .map(constr -> getFields(constr.getColumns()))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public List<Field> getForeginKeyFields() {

    List<ForigenKeyConstraint> constraints = getForigenKeys();

    return constraints
        .stream()
        .map(constr -> getField(constr.getKeyName()))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public boolean isSelfDependencyFound() {
    long count =
        forigenKeys.stream().filter(fk -> fk.getReferenceTable().equals(tableName)).count();
    if (count == 0) {
      return false;
    } else {
      return true;
    }
  }

  @JsonIgnore
  public List<ForigenKeyConstraint> getSelfDependencies() {
    return forigenKeys
        .stream()
        .filter(fk -> fk.getReferenceTable().equals(tableName))
        .collect(Collectors.toList());
  }

  @JsonIgnore
  public Field getField(String columnName) {
    return fields
        .stream()
        .filter(f -> f.getColumnName().equalsIgnoreCase(columnName))
        .findFirst()
        .get();
  }

  @JsonIgnore
  public List<Field> getFields(List<String> columnNames) {
    return fields
        .stream()
        .filter(f -> columnNames.contains(f.getColumnName()))
        .collect(Collectors.toList());
  }
}
