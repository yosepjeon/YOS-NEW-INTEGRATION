package com.yosep.msa.productapi.exception;

@SuppressWarnings("serial")
public class ProductNotFoundException extends RuntimeException{
	public ProductNotFoundException(String id) {
		super("ID: " + id + "인 Product가 존재하지 않습니다...");
	}
}
