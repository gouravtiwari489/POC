package com.datagenerator.demo.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.datagenerator.demo.service.SQLFileReadService;
import com.datagenerator.demo.utils.TableStructureExtractor;

public class CustomTokenEnhancer implements TokenEnhancer {
	@Autowired
	TableStructureExtractor tableStructureExtractor;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        /*final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("customInfo", "some_stuff_here");
        additionalInfo.put("authorities", user.getAuthorities());

        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);*/
        
        try {
        	ClassLoader classLoader = getClass().getClassLoader();
    		File file = new File(classLoader.getResource("mysqlsampledatabase.sql").getFile());
			LinkedHashMap<String, LinkedHashMap<String, String>> listmap= (LinkedHashMap<String, LinkedHashMap<String, String>>) tableStructureExtractor.getFKMap(file);
			  final Map<String, Object> additionalInfo = new HashMap<>();
			  additionalInfo.put("customInfo", listmap);
			 ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return accessToken;
    }

}
