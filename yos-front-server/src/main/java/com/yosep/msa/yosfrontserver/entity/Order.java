package com.yosep.msa.yosfrontserver.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Order {
	private String orderId;
	private String productId;
	private String senderId;
	private String senderName;
	private String receiverName;
	private String phone;
	private String postCode;
	private String roadAddr;
	private String jibunAddr;
	private String extraAddr;
	private String detailAddr;
	private boolean isBuy;
	private int state;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderRdate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderUdate;
}
