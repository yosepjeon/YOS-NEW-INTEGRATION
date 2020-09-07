package com.yosep.msa.productapi.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ProductSink {
	public static String PRODUCTQ = "productQ";
	public static String PRODUCTQ_AFTER_BILLING_FROM_ORDER_TO_PRODUCT = "productQ_after_billing_from_order_to_product";
	public static String PRODUCTQ_TEST = "productQ_test";
	
	@Input(PRODUCTQ)
	public SubscribableChannel productQ();
	
	@Input(PRODUCTQ_AFTER_BILLING_FROM_ORDER_TO_PRODUCT)
	public SubscribableChannel processAfterBilling();
	
	@Input(PRODUCTQ_TEST)
	public SubscribableChannel productQTest();
}
