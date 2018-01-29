package com.datagenerator.demo.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;


@Data
@Document
public class Domain {

	@Id
	String domainId;
	List<Object> tables;

}
