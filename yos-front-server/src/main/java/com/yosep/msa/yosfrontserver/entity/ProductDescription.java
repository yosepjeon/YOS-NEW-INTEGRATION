package com.yosep.msa.yosfrontserver.entity;


import lombok.Data;

@Data
public class ProductDescription {
	private String id;
//	private LocalDateTime productDescriptionRdate;
//	private LocalDateTime productDescriptionUdate;
	
	private String productDescriptionRdate;
	private String productDescriptionUdate;
	private String description;
}
