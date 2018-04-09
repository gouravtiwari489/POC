package com.osi.datagen.controller;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.domain.User;
import com.osi.datagen.exception.AlreadyLoggedInException;
import com.osi.datagen.service.LogoutService;
import java.io.File;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
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

  @Autowired LogoutService logoutService;

  @Value("${file.download.path}")
  private String fileDownloadPath;

  @PostMapping("/login")
  public ResponseEntity<HttpStatus> getAllEmps(@RequestBody User user) {

    if (user.getUsername().equalsIgnoreCase(DasConstants.ADMIN)
        && user.getPassword().equalsIgnoreCase(DasConstants.ADMIN)) {
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
    user.setTables(null);
    File resource = new File(fileDownloadPath + DasConstants.FILE_SEPRATOR + user.getUsername());
    if (resource.exists()) {
      if (cont) {
        logoutService.clearUserData(
            fileDownloadPath + DasConstants.FILE_SEPRATOR + user.getUsername());
      } else {
        throw new AlreadyLoggedInException("Warning! You already login somewhere");
      }
    }
    new File(fileDownloadPath + DasConstants.FILE_SEPRATOR + user.getUsername()).mkdir();
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping("/clearUserSession")
  public ResponseEntity<HttpStatus> logout(@RequestParam String userName) {
    logoutService.clearUserData(fileDownloadPath + DasConstants.FILE_SEPRATOR + userName);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
