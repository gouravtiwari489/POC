package com.osi.datagen.domain;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableList {
	
	private List<Table>  tables;
	
	
	public boolean isCyclicDependencyFound(){
	  
	  
	
	  
	   return false;
	}
	
	
	public List<Table> getChildTables(Table table){
	   return tables.stream()
	  .filter(t->t.getForigenKeys().stream().
	      anyMatch(fk->fk.getReferenceTable()
	          .equalsIgnoreCase(table.getTableName())))
	             .collect(Collectors.toList());
	}

}
