package com.yosep.msa.yosfrontserver;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootApplication
@EnableDiscoveryClient
public class YosFrontServerApplication {
	public static final String APPLICATION_LOCATIONS = "spring.config.location=" + "classpath:bootstrap.properties,"
			+ "classpath:aws.properties";

	public static void main(String[] args) {
//		SpringApplication.run(YosFrontendApplication.class, args);
		new SpringApplicationBuilder(YosFrontServerApplication.class).properties(APPLICATION_LOCATIONS).run(args);
	}
	
	@Bean
	public FilterRegistrationBean<Filter> encodingFilterBean() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setEncoding("UTF-8");
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}
}
