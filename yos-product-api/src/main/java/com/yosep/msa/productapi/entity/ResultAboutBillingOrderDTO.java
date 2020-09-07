package com.yosep.msa.productapi.entity;

import java.util.List;

public class ResultAboutBillingOrderDTO {
	private Boolean result;
	private List<String> orderIdList;
	
	public ResultAboutBillingOrderDTO(Boolean result, List<String> orderIdList) {
		this.result = result;
		this.orderIdList = orderIdList;
	}
	
	public Boolean getResult() {
		return this.result;
	}
	
	public List<String> getOrderIdList() {
		return this.orderIdList;
	}
}
