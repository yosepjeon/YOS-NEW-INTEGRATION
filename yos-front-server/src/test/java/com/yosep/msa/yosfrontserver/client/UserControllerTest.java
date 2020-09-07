package com.yosep.msa.yosfrontserver.client;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Map;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.yosfrontserver.common.BaseControllerTest;
import com.yosep.msa.yosfrontserver.common.TestDescription;
import com.yosep.msa.yosfrontserver.entity.User;
import com.yosep.msa.yosfrontserver.entity.UserDTO;
import com.yosep.msa.yosfrontserver.util.AuthUtil;

public class UserControllerTest extends BaseControllerTest {

	User user;

	UserDTO userDTO;

	@Value("security.oauth2.resource.jwt.key-value")
	String jwtKey;
	

	@Before
	public void setUp() {
		String userName = "test";
		String password = "123123123";
		String name = "jys";
		String email = "enekelx1@naver.com";
		String phone = "010-2683-2450";
		String postCode = "08302";
		String roadAddr = "서울 구로구 구로중앙로18길 59";
		String jibunAddr = "서울 구로구 구로동 97-8";
		String extraAddr = "(구로동, 코원)";
		String detailAddr = "1103호";

		user = User.builder().userName(userName).password(password).name(name).email(email).phone(phone)
				.postCode(postCode).roadAddr(roadAddr).jibunAddr(jibunAddr).extraAddr(extraAddr).detailAddr(detailAddr)
				.build();

		userDTO = UserDTO.builder().userName(userName).password(password).name(name).email(email).phone(phone)
				.postCode(postCode).roadAddr(roadAddr).jibunAddr(jibunAddr).extraAddr(extraAddr).detailAddr(detailAddr)
				.build();

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

	@Test
	@TestDescription("Auth서버에 토큰을 요청해 얻어오는 테스트")
	public void getOAuthTokenTest() {
		final String CLIENT_ID = "yoggaebi";
		final String CLIENT_SECRET = "pass";
		final String GRANT_TYPE = "refresh_token";
		final String AUTH_URL = "http://3.35.107.191:" + 8095 + "/oauth/token";
		
		RestTemplate authTemplate = new RestTemplate();
		
		String clientCredentials = CLIENT_ID + ":" + CLIENT_SECRET;
		Base64.Encoder encoder = Base64.getEncoder();
		String auth = encoder.encodeToString(clientCredentials.getBytes());
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + auth);
		
		MultiValueMap<String, String> parameters2 = new LinkedMultiValueMap<String,String>();
		parameters2.add("grant_type", "password");
		parameters2.add("password", "1231231231");
		parameters2.add("username", "test");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters2,headers);
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response2;
		try {
		response2 = authTemplate.postForEntity(AUTH_URL, request, Map.class);
		
		@SuppressWarnings("unchecked")
		Map<String,String> body2 = response2.getBody();
		System.out.println("********************************************************************");
		System.out.println("rest template 방식");
		System.out.println("access_token:" + String.valueOf(body2.get("access_token")));
		System.out.println("token_type:" + String.valueOf(body2.get("token_type")));
		System.out.println("refresh_token:" + String.valueOf(body2.get("refresh_token")));
		System.out.println("expires_in:" + String.valueOf(body2.get("expires_in")));
		System.out.println("scope:" + String.valueOf(body2.get("scope")));
		System.out.println("********************************************************************");
		}catch(HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			System.out.println(e.getRawStatusCode());
		}
		////////////////////////////////////////
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setUsername("test");
		resource.setPassword("123123123");
		resource.setAccessTokenUri("http://3.35.107.191:" + 8095 + "/oauth/token");
		resource.setClientId("yoggaebi");
		resource.setClientSecret("pass");
		resource.setGrantType("password");

		DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, clientContext);

		final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
		System.out.println("********************************************************************");
		System.out.println("OAuth2RestTemplate 방식");
		System.out.println("토큰 요청");
		System.out.println("access_token:" + accessToken.getValue());
		System.out.println("token_type:" + accessToken.getTokenType());
		System.out.println("refresh_token:" + accessToken.getRefreshToken());
		System.out.println("expires_in:" + accessToken.getExpiresIn());
		System.out.println("scope:" + accessToken.getScope());
		System.out.println("********************************************************************");
		
		///////////////////////////////////////////
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String,String>();
		parameters.add("grant_type", GRANT_TYPE);
		parameters.add("refresh_token", accessToken.getRefreshToken().getValue());
		
		request = new HttpEntity<>(parameters,headers);
		
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response;
		try {
			response = authTemplate.postForEntity(AUTH_URL, request, Map.class);
		
			System.out.println(response);
			@SuppressWarnings("unchecked")
			Map<String,String> body = response.getBody();
			System.out.println("********************************************************************");
			System.out.println("토큰 재요청");
			System.out.println("access_token:" + String.valueOf(body.get("access_token")));
			System.out.println("token_type:" + String.valueOf(body.get("token_type")));
			System.out.println("refresh_token:" + String.valueOf(body.get("refresh_token")));
			System.out.println("expires_in:" + String.valueOf(body.get("expires_in")));
			System.out.println("scope:" + String.valueOf(body.get("scope")));
			System.out.println("********************************************************************");
		}catch(HttpClientErrorException e) {
			System.out.println(e.getRawStatusCode());
			System.out.println(e.getResponseBodyAsString());
		}
	}
	
	@Test
	@TestDescription("생성된 토큰을 디코딩하는 테스트")
	public void decoderTokenTest() throws JsonMappingException, JsonProcessingException {
		final String CLIENT_ID = "yoggaebi";
		final String CLIENT_SECRET = "pass";
		final String AUTH_URL = "http://3.35.107.191:" + 8095 + "/oauth/token";
		
		String clientCredentials = CLIENT_ID + ":" + CLIENT_SECRET;
		Base64.Encoder encoder = Base64.getEncoder();
		String auth = encoder.encodeToString(clientCredentials.getBytes());
		
		RestTemplate authTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.add("Authorization", "Basic " + auth);
		
		MultiValueMap<String, String> parameters2 = new LinkedMultiValueMap<String,String>();
		parameters2.add("grant_type", "password");
		parameters2.add("username", "test2");
		parameters2.add("password", "123123123");
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters2,headers);
		@SuppressWarnings("rawtypes")
		ResponseEntity<Map> response2;
		try {
		response2 = authTemplate.postForEntity(AUTH_URL, request, Map.class);
		
		@SuppressWarnings("unchecked")
		Map<String,String> body2 = response2.getBody();
		String token = String.valueOf(body2.get("access_token"));
		Decoder decoder = Base64.getDecoder(); 
		final String[] splitJwt = token.split("\\.");
		final String headerStr = new String(decoder.decode(splitJwt[0].getBytes()));
		final String payloadStr = new String(decoder.decode(splitJwt[1].getBytes()));
		
		System.out.println("********************************************************************");
		System.out.println("rest template 방식");
		System.out.println("access_token:" + String.valueOf(body2.get("access_token")));
		System.out.println("header: " + headerStr);
		System.out.println("payload: " + payloadStr);
		System.out.println(payloadStr.indexOf("user_name"));
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> data = mapper.readValue(payloadStr,  new TypeReference<Map<String,Object>>(){});
		System.out.println(data);
		System.out.println(data.get("user_name"));
		System.out.println("********************************************************************");
		
		
		}catch(HttpClientErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			System.out.println(e.getRawStatusCode());
		}
	}
	
//	@Test
//	@TestDescription("Auth서버에 토큰 재발급 요청하는 테스트")
//	public void getOauthTokenByUsingRefreshToken() {
//		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
//
//		resource.setAccessTokenUri("http://localhost:" + 8095 + "/oauth/token");
//		resource.setClientId("yoggaebi");
//		resource.setClientSecret("pass");
//		resource.setGrantType("password");
//
//		DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();
//
//		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, clientContext);
//
//		final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
//		System.out.println("********************************************************************");
//		System.out.println("토큰 요청");
//		System.out.println("access_token:" + accessToken.getValue());
//		System.out.println("token_type:" + accessToken.getTokenType());
//		System.out.println("refresh_token:" + accessToken.getRefreshToken());
//		System.out.println("expires_in:" + accessToken.getExpiresIn());
//		System.out.println("scope:" + accessToken.getScope());
//		System.out.println("********************************************************************");
//	}
	
	@Test
	@TestDescription("유저 가져오기 테스트")
	public void getUserByUserIdTest() {
		String userId = "test";
		User user = User.builder().userName(userId).password("123123123").build();
		
		@SuppressWarnings("rawtypes")
		ResponseEntity tokenResponse = AuthUtil.getAuthToken(user);
		@SuppressWarnings("unchecked")
		Map<String,String> body = (Map<String, String>) tokenResponse.getBody();
		String accessToken = String.valueOf(body.get("access_token"));
		System.out.println(body.get("access_token"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);
//		headers.add("Authorization", "Bearer " + accessToken);
		
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
		
//		ResponseEntity getUserResponse = restTemplate.getForEntity("http://localhost:" + 8095 + "/" + userId, Map.class,request);
		@SuppressWarnings("rawtypes")
		ResponseEntity getUserResponse = restTemplate.exchange("http://3.35.107.191:" + 8095 + "/user/user-name/" + userId,HttpMethod.GET,request,Map.class);
	
		System.out.println(getUserResponse);
	}

	@Test
	@TestDescription("아이디 중복 여부를 확인하는 테스트")
	public void checkDupIdTest() throws Exception {
		/*
		 * 직접 USER API에 요청 테스트
		 */
//		ResponseEntity rs = restTemplate.getForEntity("http://localhost:8095/user/checkdupid?userName=test",
//				String.class);
//
//		System.out.println("중복 결과" + rs.getBody());
//
//		Assert.assertEquals("true", rs.getBody());

		/*
		 * 내 컨트롤러에 간접적으로 USER API에 요청 테스트
		 */
		MvcResult mvcResult = mockMvc.perform(get("/checkdupid?userName=test").param("userName", "test"))
				.andExpect(status().isOk()).andDo(print()).andReturn();

		System.out.println("********************************************************************");
		System.out.println("중복 체크");
		System.out.println();
		System.out.println(mvcResult.getResponse().getContentAsString());
		System.out.println("********************************************************************");
	}

	/*
	@Test
	@TestDescription("auth 서버에 유저 계정 생성을 확인하는 테스트")
	public void userRegisterTest() throws Exception {
		MvcResult mvcResult = mockMvc
				.perform(post("/createUser")
						.with(csrf()) // csrf를 사용하고 있을 경우에는 추가!!!
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).param("userName", "enekelx1").param("password", "123123123")
						.param("name", "전요셉")
						.param("email", "enekelx1@naver.com").param("phone", "010-2683-2450").param("postCode", "08302")
						.param("roadAddr", "서울 구로구 구로중앙로18길 59").param("jibunAddr", "서울 구로구 구로동 97-8")
						.param("extraAddr", "(구로동, 코원)").param("detailAddr", "1103호"))
				.andDo(print()).andExpect(status().is3xxRedirection()).andReturn();
		
		System.out.println(mvcResult.getResponse());
	}
	*/
	
//	@Test
//	@TestDescription("쿠키 확인 테스트")
//	public void cookieTest() {
////		@CookieValue(value="jwt", required=false)
//		Cookie cookie = new Cookie("", "");
//		
//	}

//	@Test
//	@TestDescription("auth서버 및 유저 서비스에 계정 생성 테스트")
//	public void userRegisterTest() {
//		ResponseEntity result = restTemplate.postForEntity("http://localhost:8095/user/register", userDTO, ResponseEntity.class);
//		
//		System.out.println(result.getBody());
//		
////		MvcResult mvcResult;
//	}

}
