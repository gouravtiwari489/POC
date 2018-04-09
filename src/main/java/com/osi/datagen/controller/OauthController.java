package com.osi.datagen.controller;

import com.osi.datagen.configuration.CustomInMemoryTokenStore;
import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.service.LogoutService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

  @Autowired LogoutService logoutService;

  @Autowired private ConsumerTokenServices consumerTokenServices;

  @Autowired CustomInMemoryTokenStore customInMemoryTokenStore;

  @Value("${file.download.path}")
  private String fileDownloadPath;

  @Autowired private TokenStore tokenStore;

  @PostMapping("/oauth/logout")
  @ResponseBody
  public ResponseEntity<?> revoketoken(
      HttpSession session, OAuth2Authentication authentication, @RequestParam String userName) {

    logoutService.clearUserData(fileDownloadPath +DasConstants.FILE_SEPRATOR+ userName);
    OAuth2AccessToken token = customInMemoryTokenStore.getAccessTokenWhileLogout(authentication);
    if (token != null) {
      consumerTokenServices.revokeToken(token.getValue());
      tokenStore.removeAccessToken(token);
      tokenStore.removeRefreshToken(token.getRefreshToken());
    }
    session.invalidate();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
