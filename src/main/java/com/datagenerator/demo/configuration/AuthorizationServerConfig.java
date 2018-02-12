package com.datagenerator.demo.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.datagenerator.demo.domain.CustomTokenEnhancer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
    private AuthenticationManager authenticationManager;
	
	/*@Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
//		endpoints.tokenStore(tokenStore())
			.authenticationManager(authenticationManager);
    }*/
	
	@Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory() 
	        .withClient("client") 
	        .secret("clientpassword")
	        .scopes("read", "write") 
	        .authorizedGrantTypes("password")
	        .accessTokenValiditySeconds(3600);
    }
	/*
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
	
	@Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // ...
        tokenServices.setTokenEnhancer(tokenEnhancer());
        return tokenServices;
    }

    // Some @Bean here like tokenStore

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }*/

}
