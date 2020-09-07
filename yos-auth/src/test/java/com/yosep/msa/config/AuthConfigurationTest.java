package com.yosep.msa.config;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;

import com.yosep.msa.account.YoggaebiUser;
import com.yosep.msa.account.YoggaebiUserRole;
import com.yosep.msa.account.YoggaebiUserService;
import com.yosep.msa.common.BaseControllerTest;

public class AuthConfigurationTest extends BaseControllerTest {

	@Autowired
	YoggaebiUserService yoggaebiUserService;
	
	YoggaebiUser user;
	
	@Before
	public void setUp() {
		String userName = "enekelx1";
		String password = "123123";
		String name = "jys";
		String email = "enekelx1@naver.com";
		String phone = "010-2683-2450";
		String postCode = "08302";
		String roadAddr = "서울 구로구 구로중앙로18길 59";
		String jibunAddr = "서울 구로구 구로동 97-8";
		String extraAddr = "(구로동, 코원)";
		String detailAddr = "1103호";
		
		user = YoggaebiUser.builder()
				.userName(userName)
				.password(password)
				.name(name)
				.email(email)
				.phone(phone)
				.postCode(postCode)
				.roadAddr(roadAddr)
				.jibunAddr(jibunAddr)
				.extraAddr(extraAddr)
				.detailAddr(detailAddr)
				.roles(Stream.of(YoggaebiUserRole.ADMIN,YoggaebiUserRole.USER).collect(Collectors.toSet()))
				.build();
		
		this.yoggaebiUserService.saveAccount(user);
	}

	@Test
	@Description("인증 토큰을 발급 받는 테스트")
	public void getAuthToken() throws Exception {
		// Given
		String username = "enekelx1";
		String password = "123123";
		
		String clientId = "yoggaebi";
		String clientSecret = "pass";
		
		this.mockMvc.perform(post("/oauth/token")
				.with(httpBasic(clientId, clientSecret))
				.param("username", username)
				.param("password", password)
				.param("grant_type", "password"))
		.andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("access_token").exists());
	}

}
