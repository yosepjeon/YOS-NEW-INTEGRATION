package com.yosep.msa.productapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderMessageQueueAfterBillingDTO {
	private String orderId;
	private String productName;
	
	private boolean success;
	
}
