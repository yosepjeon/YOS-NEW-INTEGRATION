package com.yosep.msa.yosfrontserver.entity;

import java.util.ArrayList;
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
public class ProductDtoForRestTemplate {
	@NotNull
	private String productId;

	@NotNull
	private String productName;

	@Min(0)
	private int productSale;

	private String productDetail;

	@NotNull
	@Min(0)
	private int productPrice;

	@NotNull
	@Min(0)
	private int productQuantity;

	private List<String> productProfileImageURLs = new ArrayList<>();
}
