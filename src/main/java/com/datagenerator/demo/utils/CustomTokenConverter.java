package com.datagenerator.demo.utils;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Scope("session")
public class CustomTokenConverter extends JwtAccessTokenConverter {

  @Autowired private AuthorizationServerTokenServices tokenServices;

  Map<String, Object> additionalInfo = new HashMap<>();

  private OAuth2Authentication authentication = null;

  @Override
  public OAuth2AccessToken enhance(
      OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

    additionalInfo.put("customized", "true");
    additionalInfo.put("isAdmin", "test authontication");
    additionalInfo.put("customInfo", "");
    ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
    this.authentication = authentication;
    return super.enhance(accessToken, authentication);
  }

  public void setAdditionalInfo(String key, Object data) {
    tokenServices.getAccessToken(authentication).getAdditionalInformation().put(key, data);
  }

  public Object getAdditionalInfo(String key) {
    return tokenServices.getAccessToken(authentication).getAdditionalInformation().get(key);
  }
}
