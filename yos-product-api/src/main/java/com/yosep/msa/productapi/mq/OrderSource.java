package com.yosep.msa.productapi.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface OrderSource {
	public static String ORDERQ_TEST = "orderQ_test";
	public static String ORDERQ_SUCCESS_BILLING_PROCESS="orderQ_success_billing_process";
	
	@Output(ORDERQ_TEST)
	public MessageChannel orderQTest();
	
	@Output(ORDERQ_SUCCESS_BILLING_PROCESS)
	public MessageChannel processSuccessBilling();
}
