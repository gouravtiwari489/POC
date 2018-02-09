package com.datagenerator.demo.controller;

import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.datagenerator.demo.domain.User;


@RestController
@CrossOrigin
public class LoginController {
	
	@Autowired
    private AuthorizationServerTokenServices tokenServices;
	
	@PostMapping("/login")
	public ResponseEntity<HttpStatus> getAllEmps(@RequestBody User user){
		
		if(user.getUsername().equalsIgnoreCase("admin") && user.getPassword().equalsIgnoreCase("admin")){
			return new  ResponseEntity<>(HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	

    @GetMapping(value = "/getSomething")
    public String getSection(OAuth2Authentication authentication) {
       Map<String, Object> additionalInfo = tokenServices.getAccessToken(authentication).getAdditionalInformation();
        String customInfo = (String) additionalInfo.get("customInfo");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) additionalInfo.get("authorities");

        // Play with authorities

        return customInfo;
    }
	
	
}
