package com.yosep.yosuserapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class YosUserApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YosUserApiApplication.class, args);
	}

}
