package com.yosep.msa.productapi.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.yosep.msa.productapi.entity.Product;
import com.yosep.msa.productapi.entity.ProductDTO;
import com.yosep.msa.productapi.entity.ProductDetailType;
import com.yosep.msa.productapi.entity.ProductType;
import com.yosep.msa.productapi.service.ProductService;

@Configuration
public class AppConfig {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@LoadBalanced
	@Bean
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ApplicationRunner applicationRunner() {
		return new ApplicationRunner() {
			@Autowired
			ProductService productService;
			
			@Override
			public void run(ApplicationArguments args) throws Exception {
				// TODO Auto-generated method stub
				
				for(int i=1;i<=5;i++) {
					List<String> urls = new ArrayList<>();
					urls.add("test" + i);
					
					Product product = Product.builder()
							.productId("test" + i)
							.productName("test" + i)
							.productPrice(10000 * i)
							.productQuantity(100)
							.productDetail("test" + i)
							.productType(ProductType.WATCH)
							.productDetailType(ProductDetailType.DIGITAL)
							.productRdate(LocalDateTime.now())
							.productUdate(LocalDateTime.now())
							.build();
					
					ProductDTO productDTO = ProductDTO.builder()
							.productProfileImageURLs(urls)
							.build();
					
					productService.create(product, productDTO);
				}
			}

		};
	}
}