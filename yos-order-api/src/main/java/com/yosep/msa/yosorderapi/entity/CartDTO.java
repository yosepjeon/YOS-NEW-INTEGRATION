package com.yosep.msa.yosorderapi.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
public class CartDTO {
	@NotBlank
	@Size(max = 300)
	private String orderId;
	
	@NotBlank
	@Size(max=30)
	private String userName;
	
	@NotBlank
	@Size(max = 300)
	private String productId;
}
