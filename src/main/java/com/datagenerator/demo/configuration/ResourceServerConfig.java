package com.datagenerator.demo.configuration;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import com.datagenerator.demo.domain.CustomUserDetails;
import com.datagenerator.demo.domain.Role;
import com.datagenerator.demo.domain.User;
import com.datagenerator.demo.repository.UserRepository;

@Configuration
@EnableResourceServer
@EnableWebSecurity
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder,UserRepository repo) throws Exception {
	    if(repo.count()==0) {
	    	repo.save(new User("user","user",Arrays.asList(new Role("USER"),new Role("ACTUATOR"))));
	    }
		                           
		builder.userDetailsService(name->new  CustomUserDetails( repo.findByUsername(name )));
    }
    
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests().antMatchers("/login**").permitAll()
				.anyRequest().authenticated().and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
			.csrf().disable();
	}
	
}
