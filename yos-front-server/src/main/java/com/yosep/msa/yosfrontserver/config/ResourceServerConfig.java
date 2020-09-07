package com.yosep.msa.yosfrontserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import com.yosep.msa.yosfrontserver.filter.CsrfHeaderFilter;

@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter{
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		// TODO Auto-generated method stub
//		http.headers().frameOptions().disable();
//		
//		http
//			.csrf()
////			.anonymous()
//				.and()
//			.authorizeRequests()
//				.mvcMatchers(HttpMethod.GET,"/user/checkdupid*")
//					.permitAll()
//				.mvcMatchers(HttpMethod.POST,"/user/createUser")
//					.permitAll()
////				.mvcMatchers(HttpMethod.GET,"/user/test")
////					.anonymous()
//				
//				.anyRequest()
//					.permitAll()
////					.authenticated()
//				.and()
//			.exceptionHandling()
//				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
//	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		
		// @formatter:off
		CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
		csrfTokenRepository.setCookieName("X-XSRF-TOKEN");
		csrfTokenRepository.setCookieHttpOnly(true);

		http.httpBasic()
			.and()
			.authorizeRequests()
				.mvcMatchers(HttpMethod.GET, "/checkdupid*").permitAll()
				.mvcMatchers(HttpMethod.POST, "/createUser").permitAll()
				.mvcMatchers(HttpMethod.POST, "/loginCheck").permitAll()
				.mvcMatchers(HttpMethod.POST).permitAll()
//						.mvcMatchers(HttpMethod.POST,"/")
//	                    .anyRequest()
//	                    	.denyAll()
				.and()
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
				.csrf()
					.csrfTokenRepository(csrfTokenRepository())
//	                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//	                	.csrfTokenRepository(csrfTokenRepository)
				.and()
				.exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());
		// @formatter:on
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("YCT-XSRF-TOKEN");
		return repository;
	}
}
