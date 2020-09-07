package com.yosep.msa.yosfrontserver.config;
//package com.yosep.msa.yosfrontend.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
//import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
//import org.springframework.security.web.csrf.CsrfFilter;
//import org.springframework.security.web.csrf.CsrfTokenRepository;
//import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
//
//import com.yosep.msa.yosfrontend.filter.CsrfHeaderFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		// TODO Auto-generated method stub
//		return super.authenticationManagerBean();
//	}
//
//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		// TODO Auto-generated method stub
//		super.configure(web);
//	}
//
////	@Override
////	protected void configure(HttpSecurity http) throws Exception {
////		// TODO Auto-generated method stub
//////		super.configure(http);
////		http
////		.authorizeRequests()
////			.mvcMatchers(HttpMethod.GET,"/user/checkdupid*")
////				.anonymous()
////			//.mvcMatchers(HttpMethod.POST,"/user/createUser")
////			//	.anonymous()
//////			.mvcMatchers(HttpMethod.GET,"/user/test")
//////				.anonymous()
////			
////			.anyRequest()
////				.denyAll()
////			.and()
////		.csrf()
////			.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////		.and();
////	}
//
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// @formatter:off
//		CookieCsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
//		csrfTokenRepository.setCookieName("X-XSRF-TOKEN");
//		csrfTokenRepository.setCookieHttpOnly(true);
//
//		http.httpBasic()
//			.and()
//			.authorizeRequests()
//				.mvcMatchers(HttpMethod.GET, "/checkdupid*").permitAll()
//				.mvcMatchers(HttpMethod.POST, "/createUser").permitAll()
//				.mvcMatchers(HttpMethod.POST, "/loginCheck").permitAll()
//				.mvcMatchers(HttpMethod.POST).permitAll()
////						.mvcMatchers(HttpMethod.POST,"/")
////	                    .anyRequest()
////	                    	.denyAll()
//				.and()
//				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class)
//				.csrf()
//					.csrfTokenRepository(csrfTokenRepository())
////	                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////	                	.csrfTokenRepository(csrfTokenRepository)
//				.and()
//			.exceptionHandling()
//			.accessDeniedHandler(new OAuth2AccessDeniedHandler());
//		// @formatter:on
//	}
//
//	private CsrfTokenRepository csrfTokenRepository() {
//		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		repository.setHeaderName("YCT-XSRF-TOKEN");
//		return repository;
//	}
//
//}
