package com.yosep.msa.yosfrontserver.util;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.yosfrontserver.entity.Order;

public class OrderUtil {
	public List<Order> orderList;
	
	@SuppressWarnings("rawtypes")
	public static ResponseEntity getOrderListBeforeBuy(String userId,String token,RestTemplate restTemplate) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
//		ObjectMapper om = new ObjectMapper();
//		Object orderList = restTemplate.getForObject("http://order-apigateway/api/order/orders-before-buy-by-userid/" + userId, Map.class);
//		List<Order> orders = om.convertValue(orderList, new TypeReference<List<Order>>() {});
		ResponseEntity response = restTemplate.exchange("http://52.78.74.150:8035/api/orders/orders-before-buy-by-userid/enekelx1", HttpMethod.GET,httpEntity,Map.class);
		
		return response;
	}
	
	public static List<Order> getOrderListAfterBuy(String userId,RestTemplate restTemplate) {
		ObjectMapper om = new ObjectMapper();
		Object orderList = restTemplate.getForObject("http://order-apigateway/api/order/getUsersOrdersAfterBuy?userId=" + userId, Object.class);
		List<Order> orders = om.convertValue(orderList, new TypeReference<List<Order>>() {});
		
		return orders;
	}
}
