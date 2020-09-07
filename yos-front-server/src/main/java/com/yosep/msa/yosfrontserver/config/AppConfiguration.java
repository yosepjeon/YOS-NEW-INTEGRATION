package com.yosep.msa.yosfrontserver.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class AppConfiguration {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	    mapper.registerModule(new JavaTimeModule());
	    return mapper;
	}
	
//	@Bean
//	public ResourceOwnerPasswordResourceDetails resourceOwnerPasswordResourceDetails() {
//		return new ResourceOwnerPasswordResourceDetails();
//	}
}
