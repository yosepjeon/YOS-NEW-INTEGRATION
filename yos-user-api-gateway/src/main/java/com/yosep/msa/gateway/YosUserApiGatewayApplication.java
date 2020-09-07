package com.yosep.msa.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class YosUserApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(YosUserApiGatewayApplication.class, args);
	}

	@LoadBalanced 
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
