package com.yosep.msa.yosfrontserver.common;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import com.yosep.msa.yosfrontserver.entity.User;

public class OAuthUtil {
	public static OAuth2AccessToken setResourceForAuth(ResourceOwnerPasswordResourceDetails resource,User user) {		
		resource = new ResourceOwnerPasswordResourceDetails();
		resource.setUsername("test");
		resource.setPassword("123123123");
		resource.setAccessTokenUri("http://localhost:" + 8095 + "/oauth/token");
		resource.setClientId("yoggaebi");
		resource.setClientSecret("pass");
		resource.setGrantType("password");

		DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource,clientContext);
		
		oAuth2RestTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
		
		System.out.println(oAuth2RestTemplate.getAccessToken().getValue().isEmpty());
		System.out.println(oAuth2RestTemplate.getAccessToken());
		
		final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
		
		return accessToken;
	}
	
	public static OAuth2AccessToken getTokenByRefreshToken(User user) {
//		resource = new AuthorizationCodeResourceDetails();
		return null;
		
	}
}
