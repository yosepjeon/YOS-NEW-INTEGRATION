package com.yosep.msa.yosfrontserver.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DtoForCreatingOrder {
	private String senderName;
	private String receiverName;
	private String phone;
	private String postCode;
	private String roadAddr;
	private String jibunAddr;
	private String extraAddr;
	private String detailAddr;
	private List<CartDTO> cart;
}
