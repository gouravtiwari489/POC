package com.datagenerator.demo;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.datagenerator.demo.domain.CustomUserDetails;
import com.datagenerator.demo.domain.Role;
import com.datagenerator.demo.domain.User;
import com.datagenerator.demo.repository.UserRepository;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
	@Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder,UserRepository repo) throws Exception {
	    if(repo.count()==0) {
	    	repo.save(new User("user","user",Arrays.asList(new Role("USER"),new Role("ACTUATOR"))));
	    }
		
	    builder.userDetailsService(name->new  CustomUserDetails( repo.findByUsername(name )));
    }
}

