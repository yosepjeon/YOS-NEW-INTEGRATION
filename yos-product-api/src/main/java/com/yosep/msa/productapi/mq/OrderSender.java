package com.yosep.msa.productapi.mq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.productapi.entity.OrderMessageQueueAfterBillingDTO;

@RefreshScope
@EnableBinding(OrderSource.class)
@Component
public class OrderSender {
	@Autowired
	ObjectMapper objectMapper;
	
	@Output(OrderSource.ORDERQ_TEST)
	@Autowired
	private MessageChannel orderQTestChannel;
	
	@Output(OrderSource.ORDERQ_SUCCESS_BILLING_PROCESS)
	@Autowired
	private MessageChannel orderSuccessBillingProcessChannel;
	
	public boolean sendToOrderQTestChannel() {
		System.out.println("send order messageQ Test");
		return orderQTestChannel.send(MessageBuilder.withPayload("order-mq-test").build());
	}
	
	public boolean sendToOrderSuccessBillingProcessChannel(List<OrderMessageQueueAfterBillingDTO> orderMessageQueueAfterBiilingDTOs) {
		String message;
		
		try {
			message = objectMapper.writeValueAsString(orderMessageQueueAfterBiilingDTOs);
			return orderSuccessBillingProcessChannel.send(MessageBuilder.withPayload(message).build());
		} catch (JsonProcessingException e) {
			return false;
		}
	}
}
