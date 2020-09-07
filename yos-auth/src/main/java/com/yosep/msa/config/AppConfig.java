package com.yosep.msa.config;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.yosep.msa.account.YoggaebiUser;
import com.yosep.msa.account.YoggaebiUserRole;
import com.yosep.msa.account.YoggaebiUserService;

@Configuration
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			
			@Autowired
			YoggaebiUserService accountService;
			
			@Override
			public void run(ApplicationArguments args) throws Exception {
				// TODO Auto-generated method stub
				String userName = "test";
				String password = "123123123";
				String name = "jys";
				String email = "test@naver.com";
				String phone = "010-2683-2450";
				String postCode = "08302";
				String roadAddr = "서울 구로구 구로중앙로18길 59";
				String jibunAddr = "서울 구로구 구로동 97-8";
				String extraAddr = "(구로동, 코원)";
				String detailAddr = "1103호";
				
				YoggaebiUser account = YoggaebiUser.builder()
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
				
				accountService.saveAccount(account);
			}
		};
	}
}
