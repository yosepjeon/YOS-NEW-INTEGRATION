package com.yosep.msa.account;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class YoggaebiUserServiceTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	YoggaebiUserService yoggaebiUserService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
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
	}

	@Test
	public void findByUserId() {
		String username = "enekelx1";
		String password = "123123";
//		String name = "jys";
//		String email = "enekelx1@naver.com";
//		String phone = "010-2683-2450";
//		String postCode = "08302";
//		String roadAddr = "서울 구로구 구로중앙로18길 59";
//		String jibunAddr = "서울 구로구 구로동 97-8";
//		String extraAddr = "(구로동, 코원)";
//		String detailAddr = "1103호";
		
		// Given
//		YoggaebiUser user = YoggaebiUser.builder()
//					.userName(userId)
//					.password(password)
//					.name(name)
//					.email(email)
//					.phone(phone)
//					.postCode(postCode)
//					.roadAddr(roadAddr)
//					.jibunAddr(jibunAddr)
//					.extraAddr(extraAddr)
//					.detailAddr(detailAddr)
//					.roles(Stream.of(YoggaebiUserRole.ADMIN,YoggaebiUserRole.USER).collect(Collectors.toSet()))
//					.build();
		
		this.yoggaebiUserService.saveAccount(user);
		
		// When
		UserDetailsService userDetailsService = (UserDetailsService) yoggaebiUserService;
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		
		// Then
		assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
	}

}
