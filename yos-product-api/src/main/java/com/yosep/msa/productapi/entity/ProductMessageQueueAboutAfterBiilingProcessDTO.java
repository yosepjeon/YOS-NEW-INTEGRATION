package com.yosep.msa.productapi.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMessageQueueAboutAfterBiilingProcessDTO {
	private String orderId;
	private String productId;
}
