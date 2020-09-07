package com.yosep.msa.yosorderapi.entity;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDTO {
	@NotBlank
	@Size(max = 300)
	private String orderId;

	@NotBlank
	@Size(max = 300)
	private String productId;
	@NotBlank
	@Size(max = 30)
	private String senderId;
	@NotBlank
	@Size(max = 50)
	private String senderName;
	@NotBlank
	@Size(max = 50)
	private String receiverName;
	@NotBlank
	@Size(max = 50)
	private String phone;
	@NotBlank
	@Size(max = 50)
	private String postCode;
	@NotNull
	@Size(max = 50)
	private String roadAddr;
	@NotNull
	@Size(max = 50)
	private String jibunAddr;
	@NotBlank
	@Size(max = 50)
	private String extraAddr;

	@Size(max = 50)
	private String detailAddr;

	private boolean isBuy;
	
	private int state;

//	@JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	private LocalDateTime orderRdate;
//	private DateType orderRdate;

//	@JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
	private LocalDateTime orderUdate;
//	private DateType orderUdate;
}
