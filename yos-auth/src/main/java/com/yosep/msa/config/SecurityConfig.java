package com.yosep.msa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.yosep.msa.account.YoggaebiUserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	YoggaebiUserService yoggaebiUserService;

	@Autowired
	PasswordEncoder passwordEncoder;

//	@Bean
//	public TokenStore tokenStore() {
//		return new InMemoryTokenStore();
//	}

	@Bean
	public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory) {
		return new RedisTokenStore(redisConnectionFactory);
	}

//	@Bean
//	public TokenStore tokenStore() {
//		return new RedisTokenStore(jedisConnectionFactory());
//	}
//
//	@SuppressWarnings("deprecation")
//	@Bean
//	public JedisConnectionFactory jedisConnectionFactory() {
//		
//		JedisConnectionFactory factory = new JedisConnectionFactory();
//		factory.setHostName("localhost");
//		factory.setPort(6379);
//		factory.setPassword("");
//		factory.setDatabase(1);
//		factory.setUsePool(true);
//
//		return factory;
//	}

	// 다른 인증서버, 리소스서버가 참조할 수 있도록 빈으로 등록 후 이 메소드 사용
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}

	// 인증관리자를 어떻게 만들거냐 재정의
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(yoggaebiUserService).passwordEncoder(passwordEncoder);
	}

	// filter를 적용할지 말지를 정의하는 곳
	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().mvcMatchers("/docs/index.html");
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//
//		http.csrf().disable().authorizeRequests()
//			.mvcMatchers(HttpMethod.POST, "/user/register").permitAll()
//			.mvcMatchers(HttpMethod.GET,"/user/checkdupid").permitAll()
//			.mvcMatchers(HttpMethod.POST,"/oauth/token").permitAll()
//				// /api로 시작하는
//				// 모든 요청을
//				// 익명을
//				// 허용하겠다.
//				.anyRequest().authenticated(); // 나머지는 인증이 필요하다.
//	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// TODO Auto-generated method stub
//		http.anonymous()
//			.and()
//			.formLogin()
//			.and()
//			.authorizeRequests()
//			.mvcMatchers(HttpMethod.GET,"/api/**").authenticated() // /api로 시작하는 모든 요청을 익명을 허용하겠다.
//			.anyRequest().authenticated(); // 나머지는 인증이 필요하다.
//	}
}
