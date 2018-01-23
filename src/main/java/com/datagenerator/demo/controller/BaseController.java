package com.datagenerator.demo.controller;

import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class BaseController {

	private String message;
	private String statusCode;
	
}
