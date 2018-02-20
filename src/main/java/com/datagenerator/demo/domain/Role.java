package com.datagenerator.demo.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Role {
	
	private String name;
	public Role() {}
	public Role( String name) {
		this.name = name;
	}
}
