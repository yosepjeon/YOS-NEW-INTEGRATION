package com.yosep.msa.yosorderapi.mq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.yosorderapi.entity.OrderMessageQueueAfterBillingDTO;
import com.yosep.msa.yosorderapi.service.OrderService;

@EnableBinding(OrderSink.class)
public class OrderReceiver {
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	OrderService orderService;
	
	@StreamListener(OrderSink.ORDERQ_TEST)
	public void requestTestAccept(String message) {
		System.out.println(message);
		
		if(message.equals("order-mq-test")) {
			System.out.println("we success receive");
		}else {
			System.out.println("we fail receive");
		}
	}
	
	@StreamListener(OrderSink.ORDERQ_SUCCESS_BILLING_PROCESS)
	public void processCheckBuyTrueAboutCompleteBillingOrders(String message) throws JsonMappingException, JsonProcessingException {
		List<OrderMessageQueueAfterBillingDTO> orderMessageQueueAfterBillingDTOs = objectMapper.readValue(message, new TypeReference<List<OrderMessageQueueAfterBillingDTO>>(){});
		
		orderService.updateOrdersAfterChangeProductStateAfterBilling(orderMessageQueueAfterBillingDTOs);
	}
}
