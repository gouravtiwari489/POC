package com.osi.datagen.configuration;

import com.osi.datagen.SimpleCorsFilter;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

public class CustomInMemoryTokenStore extends InMemoryTokenStore {

  @Autowired private SimpleCorsFilter corsFilter;

  private final ConcurrentHashMap<String, String> customAccessTokenStore =
      new ConcurrentHashMap<String, String>();
  private final ConcurrentHashMap<String, String> continueAccessTokenStore =
      new ConcurrentHashMap<String, String>();

  public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
    String portInformation =
        corsFilter.getRemoteAddress()
            + "-"
            + corsFilter.getRemotePort()
            + "-"
            + corsFilter.getRemoteBrowser();
    OAuth2AccessToken accessToken = super.getAccessToken(authentication);
    if (accessToken != null
        && customAccessTokenStore.get(accessToken.getValue()) != null
        && !customAccessTokenStore.get(accessToken.getValue()).equals(portInformation)) {
      if (continueAccessTokenStore.get(accessToken.getValue()) != null
          && !customAccessTokenStore
              .get(accessToken.getValue())
              .equals(continueAccessTokenStore.get(accessToken.getValue()))) {
        this.clearTokenStore(accessToken);
        accessToken = null;
      } else {
        continueAccessTokenStore.put(accessToken.getValue(), portInformation);
        throw new OAuth2Exception("Warning! You Already LoggedIn Somewhere");
      }
    }
    return accessToken;
  }

  public OAuth2AccessToken getAccessTokenWhileLogout(OAuth2Authentication authentication) {
    return super.getAccessToken(authentication);
  }

  public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
    String portInformation =
        corsFilter.getRemoteAddress()
            + "-"
            + corsFilter.getRemotePort()
            + "-"
            + corsFilter.getRemoteBrowser();
    customAccessTokenStore.put(token.getValue(), portInformation);
    super.storeAccessToken(token, authentication);
  }

  public void clearTokenStore(OAuth2AccessToken accessToken) {
    super.removeAccessToken(accessToken.getValue());
    super.removeRefreshToken(accessToken.getRefreshToken());
    customAccessTokenStore.remove(accessToken.getValue());
    continueAccessTokenStore.remove(accessToken.getValue());
  }
}
