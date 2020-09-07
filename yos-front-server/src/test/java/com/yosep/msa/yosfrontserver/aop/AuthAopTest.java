package com.yosep.msa.yosfrontserver.aop;

import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.web.client.HttpClientErrorException;

import com.yosep.msa.yosfrontserver.common.UnitTest;

class AuthAopTest extends UnitTest{
	@BeforeEach
	public void setup() {
		resourceForTest = new ResourceOwnerPasswordResourceDetails();
		resourceForTest.setUsername("test");
		resourceForTest.setPassword("123123123");
		resourceForTest.setAccessTokenUri("http://3.35.107.191:" + 8095 + "/oauth/token");
		resourceForTest.setClientId("yoggaebi");
		resourceForTest.setClientSecret("pass");
		resourceForTest.setGrantType("password");

		clientContextForTest = new DefaultOAuth2ClientContext();

		oAuth2RestTemplateForTest = new OAuth2RestTemplate(resourceForTest, clientContextForTest);

		accessTokenForTest = oAuth2RestTemplateForTest.getAccessToken();
	}
	
	@SuppressWarnings("rawtypes")
	@Test
	@DisplayName("user api server에 토큰 유효 검증 요청을 보내는 test")
	void test() {
//		String token = null;
		String token = accessTokenForTest.getValue();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultivaluedMap<String, String>> httpEntity = new HttpEntity<MultivaluedMap<String,String>>(headers);
		try {
			ResponseEntity response = restTemplate.exchange("http://3.35.107.191:8060/api/user/checkAuth", HttpMethod.POST,httpEntity,Map.class);
			System.out.println(response);
		}catch(HttpClientErrorException e) {
			HttpStatus httpStatus = e.getStatusCode();
			System.out.println("status code: " + httpStatus.value());
			System.out.println("status reason about code: " + httpStatus.getReasonPhrase());
		}
	}

}
