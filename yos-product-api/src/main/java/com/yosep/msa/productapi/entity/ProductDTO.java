package com.yosep.msa.productapi.entity;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	@NotNull
	private String productId;
	
	@NotNull
	private String productName;
	
	@Min(0)
	private int productSale;
	
	@NotNull
	@Min(0)
	private int productPrice;
	
	@NotNull
	@Min(0)
	private int productQuantity;
	
	private List<String> productProfileImageURLs;
	
	private String productDetail;
}
