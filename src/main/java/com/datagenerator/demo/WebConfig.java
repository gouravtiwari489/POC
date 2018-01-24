package com.datagenerator.demo;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter  {
	
@Bean
 public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
  MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
  ObjectMapper objectMapper = new ObjectMapper();
  objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  jsonConverter.setObjectMapper(objectMapper);
  return jsonConverter;
 }
 
 @Bean
  public CorsFilter corsFilter() {
	 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	 CorsConfiguration config = new CorsConfiguration();
     config.setAllowCredentials(true);
     config.addAllowedOrigin("*");
     config.addAllowedHeader("*");
     //config.addAllowedMethod("OPTIONS");
	 config.addAllowedMethod("GET");
     config.addAllowedMethod("POST");
     config.addAllowedMethod("PUT");
     config.addAllowedMethod("DELETE");
     source.registerCorsConfiguration("/**", config);
     return new CorsFilter(source);
   }

 
 @Override
 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
  converters.add(customJackson2HttpMessageConverter());
  super.extendMessageConverters(converters);
 }
}