package com.datagenerator.demo.domain;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@Document
public class Domain implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	String domainId;
	List<Object> tables;

}
