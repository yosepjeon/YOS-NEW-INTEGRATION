package com.yosep.msa.yosfrontserver.entity;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

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
	
	private String productDetail;
	
	@NotNull
	@Min(0)
	private int productPrice;
	
	@NotNull
	@Min(0)
	private int productQuantity;
	
//	private List<String> productProfileImageURLs;
	
	private List<MultipartFile> productProfileImageFiles;
	
//	private String productProfileImageURL;
}
