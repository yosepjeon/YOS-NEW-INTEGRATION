package com.yosep.msa.productapi.mq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yosep.msa.productapi.entity.ProductMessageQueueAboutAfterBiilingProcessDTO;
import com.yosep.msa.productapi.service.ProductService;

@EnableBinding(ProductSink.class)
public class ProductReceiver {
	@Autowired
	ProductService productService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	OrderSender orderSender;

	public ProductReceiver() {
		
	}

//	@ServiceActivator(inputChannel = ProductSink.PRODUCTQ)
//	public void receiveFromProductQChannel(String productId) {
//		System.out.println(productId);
//		productService.setProductQuantity(productId);
//	}
	
	@ServiceActivator(inputChannel = ProductSink.PRODUCTQ_AFTER_BILLING_FROM_ORDER_TO_PRODUCT)
	public void processRequestAboutProcessingAfterBillingAccept(String message) throws JsonMappingException, JsonProcessingException {
		List<ProductMessageQueueAboutAfterBiilingProcessDTO> productMessageQueueAboutAfterBiilingProcessDTOs = objectMapper.readValue(message, new TypeReference<List<ProductMessageQueueAboutAfterBiilingProcessDTO>>(){});
		
		productService.updateProductQuantityAfterBuy(productMessageQueueAboutAfterBiilingProcessDTOs);
	}
	
//	@ServiceActivator(inputChannel = ProductSink.PRODUCTQ_TEST)
	@StreamListener(ProductSink.PRODUCTQ_TEST)
	public void requestTestAccept(String message) {
		System.out.println(message);
		
		if(message.equals("product-mq-test")) {
			System.out.println("we success receive");
			orderSender.sendToOrderQTestChannel();
		}else {
			System.out.println("we fail receive");
		}
	}
}
