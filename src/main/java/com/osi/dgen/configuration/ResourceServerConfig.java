package com.osi.dgen.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@EnableWebSecurity
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .antMatchers(
            "/login**",
            "/lib/*",
            "/images/*",
            "/css/*",
            "/swagger-ui.js",
            "/swagger-ui.min.js",
            "/api-docs",
            "/fonts/*",
            "/api-docs/*",
            "/api-docs/default/*",
            "/o2c.html",
            "index.html",
            "/webjars/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/api-docs/**",
            "/token**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .csrf()
        .disable();
  }
}
