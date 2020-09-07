package com.yosep.msa.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class YosEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YosEurekaApplication.class, args);
	}

}
