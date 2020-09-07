package com.yosep.msa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests()
			.mvcMatchers(HttpMethod.POST, "/user/register").permitAll()
			.mvcMatchers(HttpMethod.GET,"/user/checkdupid").permitAll()
			.mvcMatchers(HttpMethod.POST,"/oauth/token").permitAll()
			.mvcMatchers(HttpMethod.GET,"/user/*").permitAll()
				// /api로 시작하는
				// 모든 요청을
				// 익명을
				// 허용하겠다.
				.anyRequest().authenticated(); // 나머지는 인증이 필요하다.
	}
}
