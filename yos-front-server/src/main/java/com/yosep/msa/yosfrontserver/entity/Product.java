package com.yosep.msa.yosfrontserver.entity;

import java.util.ArrayList;
import java.util.List;

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
public class Product {
	private String productId;
	private String productName;
	private int productSale;
	private int productPrice;
	private int productQuantity;
	private String productDetail;
	private ProductType productType;
	private ProductDetailType productDetailType;
//	private LocalDateTime productRdate;
//	private LocalDateTime productUdate;
	private String productRdate;
	private String productUdate;
	private List<ProductDescription> productDescriptions = new ArrayList<ProductDescription>();
	private List<ProductProfileFile> productProfileImageURLs = new ArrayList<ProductProfileFile>();
}
