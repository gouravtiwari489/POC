package com.datagenerator.demo.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateDataObject {

	private String tableName;
	private List<GenerateDataObject> childTableName;
	
	public String toString() { 
	    return  this.tableName + "-->" + this.childTableName;
	} 
}
