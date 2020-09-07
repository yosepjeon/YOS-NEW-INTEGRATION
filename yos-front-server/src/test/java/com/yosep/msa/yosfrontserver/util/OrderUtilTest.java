package com.yosep.msa.yosfrontserver.util;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.converters.Auto;
import com.yosep.msa.yosfrontserver.common.BaseControllerTest;
import com.yosep.msa.yosfrontserver.entity.OrderDTO;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderUtilTest extends BaseControllerTest{
	@Autowired
	RestTemplate restTemplate;
	
	@Auto
	ResourceOwnerPasswordResourceDetails resourceForTest;

	OAuth2AccessToken accessTokenForTest;
	DefaultOAuth2ClientContext clientContextForTest;
	OAuth2RestTemplate oAuth2RestTemplateForTest;
	
	@BeforeEach
	public void setup() {
		resourceForTest = new ResourceOwnerPasswordResourceDetails();
		resourceForTest.setUsername("test");
		resourceForTest.setPassword("123123123");
		resourceForTest.setAccessTokenUri("http://3.35.107.191:" + 8095 + "/oauth/token");
		resourceForTest.setClientId("yoggaebi");
		resourceForTest.setClientSecret("pass");
		resourceForTest.setGrantType("password");

		clientContextForTest = new DefaultOAuth2ClientContext();

		oAuth2RestTemplateForTest = new OAuth2RestTemplate(resourceForTest, clientContextForTest);

		accessTokenForTest = oAuth2RestTemplateForTest.getAccessToken();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@DisplayName("아직 구매하지 않은 주문목록 가져오기 테스트")
	void getOrderListBeforeBuyTest() {

		String token = accessTokenForTest.getValue();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		System.out.println(token);
		ResponseEntity response = restTemplate.exchange("http://52.78.5.149:8030/api/orders/orders-before-buy-by-userid/enekelx1", HttpMethod.GET,httpEntity,Map.class);
		Map<String, Object> body = (Map<String, Object>) response.getBody();
		System.out.println(body);
		
		List<EntityModel<OrderDTO>> orderResources = (List<EntityModel<OrderDTO>>) body.get("body");
		
//		System.out.println(orderResources.get(0));
		
		
//		ObjectMapper om = new ObjectMapper();
//		List<OrderDTO> orders = om.convertValue(body.get("body"), new TypeReference<List<OrderDTO>>() {});
//		System.out.println(orders);
//		System.out.println(response);
		//		System.out.println(response);
		//		List<Order> orders = om.convertValue(orderList, new TypeReference<List<Order>>() {});
		
	}

}
