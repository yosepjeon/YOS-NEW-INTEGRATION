package com.yosep.yosuserapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.headers().frameOptions().disable();
		
		http
			.csrf().disable()
			.anonymous()
				.and()
			.authorizeRequests()
				.mvcMatchers(HttpMethod.GET,"/api/**").permitAll()
//				.mvcMatchers(HttpMethod.GET,"/user/test")
//					.anonymous()
				
				.anyRequest()
					.authenticated()
				.and()
			.exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
	}
}
