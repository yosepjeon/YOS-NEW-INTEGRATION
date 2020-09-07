package com.yosep.msa.yosorderapi.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ProductSource {
	public static String PRODUCTQ = "productQ";
	public static String PRODUCTQ_AFTER_BILLING_FROM_ORDER_TO_PRODUCT = "productQ_after_billing_from_order_to_product";
	public static String PRODUCTQ_TEST = "productQ_test";
	
	@Output(PRODUCTQ)
	public MessageChannel productQ();
	
	@Output(PRODUCTQ_AFTER_BILLING_FROM_ORDER_TO_PRODUCT)
	public MessageChannel processAfterBilling();
	
	@Output(PRODUCTQ_TEST)
	public MessageChannel productQTest();
}
