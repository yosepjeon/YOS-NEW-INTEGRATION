package com.yosep.msa.yosorderapi.mq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.yosorderapi.entity.ProductMessageQueueAboutAfterBiilingProcessDTO;
import com.yosep.msa.yosorderapi.util.JsonUtil;

import net.minidev.json.JSONArray;

@RefreshScope
@EnableBinding(ProductSource.class)
@Component
public class ProductSender {
	@Autowired
	ObjectMapper objectMapper;
	
	public ProductSender() {
		
	}
	
	@Output(ProductSource.PRODUCTQ)
	@Autowired
	private MessageChannel productQChannel;
	
	@Output(ProductSource.PRODUCTQ_AFTER_BILLING_FROM_ORDER_TO_PRODUCT)
	@Autowired
	private MessageChannel productQAfterBillingChannel;
	
	@Output(ProductSource.PRODUCTQ_TEST)
	@Autowired
	private MessageChannel productQTestChannel;
	
	public boolean sendToProductQChannel(String productId) {
		return productQChannel.send(MessageBuilder.withPayload(productId).build());
	}
	
	public boolean sendToproductQAfterBillingChannel(List<ProductMessageQueueAboutAfterBiilingProcessDTO> productIdsAboutOrders)  {
		/*
		List<Map<String,Object>> mapList = new ArrayList<>();
		
		productIdsAboutOrders.forEach(element -> {
			Map<String,Object> map = new HashMap<>();
			
			map.put("orderId", element.getOrderId());
			map.put("productId", element.getProductId());
			
			mapList.add(map);
		});
		
		JSONArray convertedProductIdsAboutOrders = JsonUtil.getJsonArrayFromList(mapList);
		*/
		
		String message;
		try {
			message = objectMapper.writeValueAsString(productIdsAboutOrders);
			return productQAfterBillingChannel.send(MessageBuilder.withPayload(message).build());
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
//		return productQAfterBillingChannel.send(MessageBuilder.withPayload(productIdsAboutOrders).build());
	}
	
	public boolean sendToProductQTestChannel() {
		System.out.println("send product messageQ Test");
		return productQTestChannel.send(MessageBuilder.withPayload("product-mq-test").build());
	}
}

