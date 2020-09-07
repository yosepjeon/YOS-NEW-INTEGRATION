package com.yosep.msa.yosfrontserver.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yosep.msa.yosfrontserver.common.BaseControllerTest;
import com.yosep.msa.yosfrontserver.entity.CartDTO;
import com.yosep.msa.yosfrontserver.entity.OrderDTO;
import com.yosep.msa.yosfrontserver.entity.OrderState;
import com.yosep.msa.yosfrontserver.util.AuthUtil;
import com.yosep.msa.yosfrontserver.util.OrderUtil;

class OrderControllerTest extends BaseControllerTest {
	
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@DisplayName("주문 완료 후에 주문한 상품들을 보여주는 장바구니 구현을 위한 테스트")
	public void showCartPageTest() throws JsonMappingException, JsonProcessingException {
		String token = accessTokenForTest.getValue();
		@SuppressWarnings("unused")
		Map<String, Object> tokenPayload = AuthUtil.decodeTokenPayload(token);
		Map<String,Object> payload = AuthUtil.decodeTokenPayload(token);
		System.out.println(payload);
		String userId = String.valueOf(payload.get("user_name"));
		
		ResponseEntity response = OrderUtil.getOrderListBeforeBuy(userId, token, oAuth2RestTemplateForTest);
		Map<String, Object> responseBody = (Map<String, Object>)response.getBody();
		System.out.println("responseBody: " + responseBody);
		List<LinkedHashMap<String, Object>> orders = (List<LinkedHashMap<String, Object>>) responseBody.get("body");
		
		int totalPrice = 0;
		
		List<LinkedHashMap<String, Object>> products = new ArrayList<>();
		
		System.out.println("order Size: " + orders.size());
		
		System.out.println("<------------------------ List ---------------------->");
		for(LinkedHashMap<String,Object> orderDTO : orders) {
			System.out.println("order - " + orderDTO);
			ResponseEntity productResponse = restTemplate.getForEntity("http://52.78.74.150:8075/api/products/" + orderDTO.get("productId"), Map.class);
			
			LinkedHashMap<String, Object> product = (LinkedHashMap<String, Object>) productResponse.getBody();
			products.add(product);
			System.out.println("product - " + product);
			totalPrice += Integer.valueOf(String.valueOf(product.get("productPrice")));
//			System.out.println(product.size());
		}
		System.out.println("products");
		System.out.println(products);
		System.out.println(totalPrice);

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	@DisplayName("주문 완료 후에 주문한 상품들을 보여주는 장바구니 구현을 위한 테스트 v2")
	public void showCartPageTestVersion2() throws JsonMappingException, JsonProcessingException {
		String token = accessTokenForTest.getValue();
		@SuppressWarnings("unused")
		Map<String, Object> tokenPayload = AuthUtil.decodeTokenPayload(token);
		Map<String,Object> payload = AuthUtil.decodeTokenPayload(token);
		System.out.println("token payload");
		System.out.println(payload);
		String userId = String.valueOf(payload.get("user_name"));
		userId = "enekelx1";
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		ResponseEntity getCartResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart/" + userId, HttpMethod.GET,httpEntity,Map.class);
		Map<String, Object> getCartResponseBody = (Map<String, Object>) getCartResponse.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) getCartResponseBody.get("body");
		
		int totalPrice = 0;
		
		List<String> productIdList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> products = new ArrayList<>();
		
		cartElements.forEach(e -> {
			String productId = String.valueOf(e.get("productId"));
			productIdList.add(productId);
		});
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://3.34.155.11:8070/api/products/cart")
				.queryParam("productIdList", productIdList);
		ResponseEntity getProductResponse = restTemplate.exchange(uriComponentsBuilder.toUriString() , HttpMethod.GET,httpEntity,List.class);
		List<LinkedHashMap<String, Object>> getProductResponseBody = (List<LinkedHashMap<String, Object>>) getProductResponse.getBody();

		for(LinkedHashMap<String, Object> product : getProductResponseBody) {
			products.add(product);
			totalPrice += Integer.parseInt(String.valueOf(product.get("productPrice")));
		};
		
		System.out.println("products");
		System.out.println(products);
		System.out.println(totalPrice);

	}

	@SuppressWarnings({"rawtypes", "unchecked" })
	@Test
	@DisplayName("결제 페이지 구현을 위한 테스트")
	public void showPayPageTest() throws JsonMappingException, JsonProcessingException {
		String token = accessTokenForTest.getValue();
		Map<String, Object> payload = AuthUtil.decodeTokenPayload(token);
		String userId = String.valueOf(payload.get("user_name"));
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		ResponseEntity getCartResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart/" + userId, HttpMethod.GET,httpEntity,Map.class);
		Map<String, Object> getCartResponseBody = (Map<String, Object>) getCartResponse.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) getCartResponseBody.get("body");
		
		List<String> productIdList = new ArrayList<>();
		cartElements.forEach(e -> {
			String productId = String.valueOf(e.get("productId"));
			productIdList.add(productId);
		});
		System.out.println("--------cart-------------");
		System.out.println(cartElements);
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://3.34.155.11:8070/api/products/cart")
				.queryParam("productIdList", productIdList);
		ResponseEntity getProductResponse = restTemplate.exchange(uriComponentsBuilder.toUriString() , HttpMethod.GET,httpEntity,List.class);
		List<LinkedHashMap<String, Object>> getProductResponseBody = (List<LinkedHashMap<String, Object>>) getProductResponse.getBody();
		
		List<OrderDTO> orderList = new ArrayList<>();
		int totalPrice = 0;
		
		cartElements.forEach(e -> {
			
			OrderDTO orderForPost = OrderDTO.builder()
					.orderId(String.valueOf(e.get("orderId")))
					.productId(String.valueOf(e.get("productId")))
					.senderId(String.valueOf(e.get("userName")))
					.senderName("전요셉")
					.phone("010-1234-1234")
					.postCode("12345")
					.roadAddr("abc")
					.jibunAddr("abc")
					.extraAddr("abc")
					.detailAddr("abc")
					.isBuy(false)
					.state(OrderState.WAIT)
					.build();
			
			orderList.add(orderForPost);
		});
		List<LinkedHashMap<String, Object>> products = new ArrayList<>();
		
		System.out.println("-----------products--------------");
		for(LinkedHashMap<String, Object> product : getProductResponseBody) {
			totalPrice += Integer.parseInt(String.valueOf(product.get("productPrice")));
			products.add(product);
			System.out.println(product);
		};

		ResponseEntity response = OrderUtil.getOrderListBeforeBuy(userId, token, restTemplate);
		Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
		System.out.println("responseBody: " + responseBody);
		List<LinkedHashMap<String, Object>> orders = (List<LinkedHashMap<String, Object>>) responseBody.get("body");
		
		System.out.println(totalPrice);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@DisplayName("유저 아이디를 기준으로 장바구니 가져오기 테스트")
	public void getCartByUserId() {
		HttpHeaders headers = new HttpHeaders();
		String token = accessTokenForTest.getValue();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		
		ResponseEntity response = restTemplate.exchange("http://52.78.74.150:8035/api/cart/test", HttpMethod.GET,httpEntity,Map.class);
		Map<String,Object> responseBody = (Map<String,Object>) response.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) responseBody.get("body");
		
		cartElements.forEach(c -> {
			System.out.println(c.toString());
		});
	}
	
	@SuppressWarnings("unchecked")
	@Test
	@DisplayName("장바구니 추가 테스트")
	public void addCartElementTest() throws JsonMappingException, JsonProcessingException {
		String token = accessTokenForTest.getValue();
		Map<String,Object> payload = AuthUtil.decodeTokenPayload(token);
		String userId= String.valueOf(payload.get("user_name"));
		UUID uuid = UUID.randomUUID();
		String productId = "test1";
		
		CartDTO cartElement = CartDTO.builder()
				.orderId(userId + "-" + productId + "-" + uuid)
				.userName(userId)
				.productId(productId)
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<CartDTO> httpEntityForCreateOrder = new HttpEntity<>(cartElement,headers);
		
		@SuppressWarnings("rawtypes")
		ResponseEntity addCartElementResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart", HttpMethod.POST,httpEntityForCreateOrder,Map.class);
	
		System.out.println(addCartElementResponse);
		System.out.println("http status: " + addCartElementResponse.getStatusCodeValue());
		
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) addCartElementResponse.getBody();
		System.out.println(
				"orderId= " + result.get("orderId") + "\n" +
				"userName= " + result.get("userName") + "\n" + 
				"productId= " + result.get("productId") + "\n" +
				"rDate= " + result.get("orderRdate") + "\n" + 
				"uDate= " + result.get("orderUdate")
				);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked" })
	@Test
	@DisplayName("결제 페이지 구현을 위한 테스트")
	public void processPayTest() throws JsonMappingException, JsonProcessingException {
		String token = accessTokenForTest.getValue();
		Map<String,Object> payload = AuthUtil.decodeTokenPayload(token);
		String userId= String.valueOf(payload.get("user_name"));
		
		OrderDTO orderForTest = OrderDTO.builder()
				.orderId("test1")
				.senderId(userId)
				.productId("test1")
				.isBuy(false)
				.state(0)
				.build();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<OrderDTO> httpEntity = new HttpEntity<>(orderForTest,headers);
		
		ResponseEntity processPayResponse;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test() {
		HttpHeaders headers = new HttpHeaders();
		String token = accessTokenForTest.getValue();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		
		String senderName = "test";
		String senderId = "test";
		String receiverName = "test";
		String phone = "test";
		String postCode = "test";
		String roadAddr = "test";
		String jibunAddr = "test";
		String extraAddr = "test";
		String detailAddr = "test";
		
		ResponseEntity response = restTemplate.exchange("http://52.78.74.150:8035/api/cart/test", HttpMethod.GET,httpEntity,Map.class);
		Map<String,Object> responseBody = (Map<String,Object>) response.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) responseBody.get("body");
		List<OrderDTO> orderList = new ArrayList<>();
		List<CartDTO> cart = new ArrayList<>();
		
		cartElements.forEach(element -> {
			OrderDTO order = OrderDTO.builder()
					.orderId(String.valueOf(element.get("orderId")))
					.productId(String.valueOf(element.get("productId")))
					.senderId(String.valueOf(element.get("userName")))
					.senderName(senderName)
					.receiverName(receiverName)
					.phone(phone)
					.postCode(postCode)
					.roadAddr(roadAddr)
					.jibunAddr(jibunAddr)
					.extraAddr(extraAddr)
					.detailAddr(detailAddr)
					.isBuy(false)
					.state(OrderState.WAIT)
					.build();
			
			orderList.add(order);
		});
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<List<OrderDTO>> httpEntityForCreateOrder = new HttpEntity<>(orderList, headers);
		ResponseEntity createdOrdersResponse = restTemplate.exchange("http://52.78.74.150:8035/api/orders/order-list", HttpMethod.POST, httpEntityForCreateOrder, ResponseEntity.class);
		
//		System.out.println(createdOrdersResponse.getBody());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@DisplayName("유저 정보를 가져오는 테스트")
	public void getUserTest() throws JsonMappingException, JsonProcessingException {
		String userId = "enekelx1";
		ResponseEntity response = restTemplate.getForEntity("http://3.35.107.191:8095/user/" + userId, Map.class);
		LinkedHashMap<String, Object> responseBody = (LinkedHashMap<String, Object>) response.getBody();
		
		System.out.println(responseBody);
		
		String token = accessTokenForTest.getValue();
		Map<String,Object> payload = AuthUtil.decodeTokenPayload(token);
		System.out.println(payload);
		String userName= String.valueOf(payload.get("user_name"));
		
		System.out.println(userName);
	}
}

