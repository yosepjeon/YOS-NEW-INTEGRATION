package com.yosep.msa.productapi.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="productId")
@Table(name="yoggaebi_product_jpa")
@ToString
@Entity
public class Product {
	@Id
	@Column(name="product_id",length=300)
	private String productId;
	
	@Column(name="product_name",length=100, nullable=false)
	private String productName;
	
	@Column(name="product_sale")
	private int productSale;
	
	@Column(name="product_price",nullable=false)
	private int productPrice;
	
	@Column(name="product_quantity",nullable=false)
	private int productQuantity;
	
	@Column(name="product_detail")
	private String productDetail;
	
	@Column(name="product_type")
	@Enumerated(EnumType.STRING)
	private ProductType productType;
	
	@Column(name="product_detail_type")
	@Enumerated(EnumType.STRING)
	private ProductDetailType productDetailType;
	
	@Column(name="product_rdate")
	private LocalDateTime productRdate;
	
	@Column(name="product_udate")
	private LocalDateTime productUdate;
	
	@JsonManagedReference
	@OneToMany(mappedBy="product",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<ProductDescription> productDescriptions = new ArrayList<ProductDescription>();

	@JsonManagedReference
	@OneToMany(mappedBy="product",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	@NotFound(action = NotFoundAction.IGNORE)
	private List<ProductProfileFile> productProfileImageURLs = new ArrayList<ProductProfileFile>();
}
