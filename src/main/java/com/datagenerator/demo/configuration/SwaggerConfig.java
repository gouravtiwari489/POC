package com.datagenerator.demo.configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Autowired
	private TypeResolver typeResolver;
	
	@Bean
	public Docket productApi() {

		List<ResponseMessage> list = new java.util.ArrayList<ResponseMessage>();
		list.add(new ResponseMessageBuilder().code(500).message("500 message")
				.responseModel(new ModelRef("JSONResult«string»")).build());
		list.add(new ResponseMessageBuilder().code(401).message("Unauthorized")
				.responseModel(new ModelRef("JSONResult«string»")).build());

		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.datagenerator.demo.controller")).paths(PathSelectors.any()).build()
				.directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(new AlternateTypeRule(
						typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false).apiInfo(metaData()).globalResponseMessage(RequestMethod.GET, list)
				.globalResponseMessage(RequestMethod.POST, list);

	}

	private ApiInfo metaData() {
		@SuppressWarnings("rawtypes")
		ApiInfo apiInfo = new ApiInfo("Spring Boot REST API", "Spring Boot REST API for IRecruit Service", "1.0",
				"Terms of service",
				new Contact("OSI Tech Portal", "http://localhost:9080/datagenerator", "ositechportal@gmail.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0",
				new ArrayList<VendorExtension>());
		return apiInfo;
	}
}
