package com.osi.datagen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class TableList implements Iterable<Table> {

  private List<Table> tables;

  @JsonIgnore
  public boolean isCyclicDependencyFound() {

    return false;
  }

  @JsonIgnore
  public List<Table> getChildTables(Table table) {
    return tables
        .stream()
        .filter(
            t ->
                t.getForigenKeys()
                    .stream()
                    .anyMatch(fk -> fk.getReferenceTable().equalsIgnoreCase(table.getTableName())))
        .collect(Collectors.toList());
  }

  @Override
  public Iterator<Table> iterator() {
    return tables.iterator();
  }
}
