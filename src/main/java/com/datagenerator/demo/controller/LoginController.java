package com.datagenerator.demo.controller;

import com.datagenerator.demo.domain.CustomUserDetails;
import com.datagenerator.demo.domain.User;
import com.datagenerator.demo.exception.AlreadyLoggedInException;
import com.datagenerator.demo.serviceImpl.LogoutService;
import com.datagenerator.demo.utils.CustomTokenConverter;

import java.io.File;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Scope("request")
@RestController
@CrossOrigin
public class LoginController {

  @Autowired private AuthorizationServerTokenServices tokenServices;

  @Autowired private CustomTokenConverter customTokenConverter;

  @Autowired LogoutService logoutService;

  @PostMapping("/login")
  public ResponseEntity<HttpStatus> getAllEmps(@RequestBody User user) {

    if (user.getUsername().equalsIgnoreCase("admin")
        && user.getPassword().equalsIgnoreCase("admin")) {
      return new ResponseEntity<>(HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/getInfo/{key}", method = RequestMethod.GET)
  public ResponseEntity<?> getSection(
      @PathVariable String key, OAuth2Authentication authentication) {
    Map<String, Object> additionalInfo =
        tokenServices.getAccessToken(authentication).getAdditionalInformation();
    return new ResponseEntity<>(additionalInfo.get(key), HttpStatus.OK);
  }

  @GetMapping("/currentlyLoggedIn/{cont}")
  public ResponseEntity<?> getUsersById(@PathVariable Boolean cont) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
    customTokenConverter.setAdditionalInfo("CurrentUser", user);
	Resource resource = new ClassPathResource("output\\" + user.getUsername());
	if (resource.exists()) {
		if (cont) {
			logoutService.clearUserData("\\output\\" + user.getUsername());
		} else {
			throw new AlreadyLoggedInException("Warning! You already login somewhere");
		}

	}
	new File("bin\\output\\" + user.getUsername()).mkdir();
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/clearUserSession")
  public ResponseEntity<HttpStatus> logout(@RequestParam String userName) {
	logoutService.clearUserData("output/"+userName);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
