package com.datagenerator.demo.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Generated;

@Document
@Data
public class Role {
	
	private String name;
	public Role() {}
	public Role( String name) {
		this.name = name;
	}
	

}
