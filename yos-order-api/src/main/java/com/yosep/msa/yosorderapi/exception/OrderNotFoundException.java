package com.yosep.msa.yosorderapi.exception;

@SuppressWarnings("serial")
public class OrderNotFoundException extends RuntimeException{
	public OrderNotFoundException(String id) {
		super("ID: " + id + "인 Order가 존재하지 않습니다...");
	}
}
