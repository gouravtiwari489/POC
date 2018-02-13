package com.datagenerator.demo.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Generated;

@Document
@Data
public class User {

	@Id
	@Generated
	private String id;
	private String username;
	private String password;
	private List<Role> roles;
	User(){}
	public User(String username, String password, List<Role> roles) {
		this.username = username;
		this.password = password;
		this.roles = roles;
	}
	
	
}
