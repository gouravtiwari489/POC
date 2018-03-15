package com.osi.dgen.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Table implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tableName;
	private List<Field> fields;

}
