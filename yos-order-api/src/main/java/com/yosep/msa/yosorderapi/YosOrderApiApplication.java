package com.yosep.msa.yosorderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class YosOrderApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(YosOrderApiApplication.class, args);
	}

}
