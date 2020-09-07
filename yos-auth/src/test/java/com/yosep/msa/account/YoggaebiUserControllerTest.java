package com.yosep.msa.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import com.yosep.msa.common.BaseControllerTest;
import com.yosep.msa.common.TestDescription;

public class YoggaebiUserControllerTest extends BaseControllerTest {

	@Autowired
	YoggaebiUserService yoggaebiUserService;

	@Autowired
	RestTemplate restTemplate;

	YoggaebiUser user;
	
	YoggaebiUserDTO userDTO;

	@Before
	public void setUp() {
		String userName = "enekelx1";
		String password = "12345678";
		String name = "jys";
		String email = "enekelx1@naver.com";
		String phone = "010-2683-2450";
		String postCode = "08302";
		String roadAddr = "서울 구로구 구로중앙로18길 59";
		String jibunAddr = "서울 구로구 구로동 97-8";
		String extraAddr = "(구로동, 코원)";
		String detailAddr = "1103호";

		user = YoggaebiUser.builder().userName(userName).password(password).name(name).email(email).phone(phone)
				.postCode(postCode).roadAddr(roadAddr).jibunAddr(jibunAddr).extraAddr(extraAddr).detailAddr(detailAddr)
				.roles(Stream.of(YoggaebiUserRole.ADMIN, YoggaebiUserRole.USER).collect(Collectors.toSet())).build();
		
		userDTO = YoggaebiUserDTO.builder().userName(userName).password(password).name(name).email(email).phone(phone)
				.postCode(postCode).roadAddr(roadAddr).jibunAddr(jibunAddr).extraAddr(extraAddr).detailAddr(detailAddr).build();
	}

	@Test
	public void testOAuthService() {
		ResourceOwnerPasswordResourceDetails resource = new ResourceOwnerPasswordResourceDetails();
		resource.setUsername("test");
		resource.setPassword("123123");
		resource.setAccessTokenUri("http://localhost:" + 8095 + "/oauth/token");
		resource.setClientId("yoggaebi");
		resource.setClientSecret("pass");
		resource.setGrantType("password");

		DefaultOAuth2ClientContext clientContext = new DefaultOAuth2ClientContext();

		OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(resource, clientContext);

		final OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();
		System.out.println("access_token: " + accessToken.getValue());
//		Greet greet = restTemplate.getForObject("http://localhost:" + port, Greet.class);

//		Assert.assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODg2NTc2MDMsInVzZXJfbmFtZSI6InRlc3QiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIiwiUk9MRV9VU0VSIl0sImp0aSI6ImQ3ZjM5YzQwLWI0ZjgtNDk1ZS1iNGQ0LWVjOTVjODQyMzNhZiIsImNsaWVudF9pZCI6InlvZ2dhZWJpIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl19.diY-I4eEMovzKyAjy8eqm8tZT030TwWW6_OjFoSvY48", accessToken.getValue());

//		Assert.assertEquals("Hello World!", greet.getMessage());

	}

	@Test
	@TestDescription("인증 서버에 유저를 생성하는 테스트")
	public void createAuthUser() throws Exception {
		System.out.println("인증 서버에 유저를 생성하는 테스트");
		System.out.println(objectMapper.writeValueAsString(userDTO));
		
		MvcResult mvcResult = mockMvc.perform(post("/user/register")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON_VALUE)
				.content(objectMapper.writeValueAsString(userDTO))).andExpect(status().isCreated()).andReturn();
		
		System.out.println("[생성 결과] \n" + mvcResult.getResponse().getContentAsString());
	}

	@Test
	public void checkDupId() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/user/checkdupid?userName=test")).andExpect(status().isOk())
				.andReturn();

		System.out.println("중복 체크 결과: " + mvcResult.getResponse().getContentAsString());
	}

//	@Test
//	@TestDescription("유저 등록을 위한 테스트")
//	public void registerTest() throws Exception {
//		MvcResult mvcResult = mockMvc.perform(post("/user/register")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(user)))
//				.andExpect(status().isOk())
//				.andReturn();
//		
//		System.out.println(mvcResult.getResponse().getContentAsString());
//	}

}
