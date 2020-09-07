package com.yosep.msa.yosfrontserver.util;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.yosfrontserver.entity.User;

public class AuthUtil {
	final static public String CLIENT_ID = "yoggaebi";
	final static public String CLIENT_SECRET = "pass";
	final static public String GRANT_TYPE = "refresh_token";
	final static public String AUTH_URL = "http://3.35.107.191:" + 8095 + "/oauth/token";
	
	@Autowired
	ModelMapper mapper;
	
	public static final String USER_TOKEN = "yos-user-token";
	public static final String USER_TOKEN_EXPIRATION = "yos-user-expiration";
	public static final String USER_TOKEN_REFRESH_EXPIRATION = "yos-user-refresh-token";
	
	public static Cookie getAuthCookie(Cookie[] userCookies) {
		Cookie userAuthCookie = null;
		
		for(Cookie userCookie : userCookies) {
			if(USER_TOKEN.equals(userCookie.getName())) {
				userAuthCookie = userCookie;
			}
		}
		
		return userAuthCookie;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map decodeTokenPayload(String tokenString) throws JsonMappingException, JsonProcessingException {
		Decoder decoder = Base64.getDecoder(); 
		final String[] splitJwt = tokenString.split("\\.");
		@SuppressWarnings("unused")
		final String headerStr = new String(decoder.decode(splitJwt[0].getBytes()));
		final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = mapper.readValue(payloadStr,  new TypeReference<Map<String,Object>>(){});
		
		return data;
	}
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity getAuthToken(User user) {
		RestTemplate authTemplate = new RestTemplate();
		
		String clientCredentials = CLIENT_ID + ":" + CLIENT_SECRET;
		Base64.Encoder encoder = Base64.getEncoder();
		String auth = encoder.encodeToString(clientCredentials.getBytes());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + auth);
		
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("grant_type", "password");
		parameters.add("username", user.getUserName());
		parameters.add("password", user.getPassword());
		
		///////
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters,headers);
		ResponseEntity<Map> response;
		try {
		response = authTemplate.postForEntity(AUTH_URL, request, Map.class);
		
		@SuppressWarnings("unchecked")
		Map<String,String> body = response.getBody();
		@SuppressWarnings("unused")
		String token = String.valueOf(body.get("access_token"));
		
		return response;
		}catch(HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			System.out.println(e.getRawStatusCode());
			return ResponseEntity.badRequest().build();
		}
	}
}
