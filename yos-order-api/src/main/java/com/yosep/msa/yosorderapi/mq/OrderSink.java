package com.yosep.msa.yosorderapi.mq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface OrderSink {
	public static String ORDERQ_TEST = "orderQ_test";
	public static String ORDERQ_SUCCESS_BILLING_PROCESS="orderQ_success_billing_process";
	
	@Input(ORDERQ_TEST)
	public SubscribableChannel orderQTest();
	
	@Input(ORDERQ_SUCCESS_BILLING_PROCESS)
	public SubscribableChannel orderSuccessBillingProcess();
	
}
