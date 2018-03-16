package com.osi.datagen.domain;

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
	List<Constraint> constraints;
	List<ForigenKeyConstraint> forigenKeys;
	List<CheckConstraint> checkConstraints;

}
