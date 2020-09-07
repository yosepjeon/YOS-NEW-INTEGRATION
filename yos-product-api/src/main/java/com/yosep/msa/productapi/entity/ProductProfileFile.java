package com.yosep.msa.productapi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Table(name="yoggaebi_product_profile_files_jpa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="fileId")
@Entity
@ToString
public class ProductProfileFile {
	@Id
	@Column(name="file_id",length=300)
	private String fileId;
	
//	@Column(name="file_name",length=100,nullable=false)
//	private String fileName;
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="product_id")
	private Product product;
	
	@Column(name="url",length=300,nullable=false)
	private String url;
	
	@Column(name="rdate")
	private LocalDateTime rdate;
	
	@Column(name="udate")
	private LocalDateTime udate;
}

//package com.yosep.msa.yosproductapi.entity;
//
//import java.time.LocalDateTime;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.EqualsAndHashCode;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//import lombok.ToString;
//
//@Entity
//@Table(name="yoggaebi_product_image_jpa")
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@EqualsAndHashCode(of="fileId")
//@ToString
//public class ProductImageFile {
//	@Id
//	@Column(name="file_id",length=300)
//	private String fileId;
//	
//	@Column(name="file_name",length=300,nullable=false)
//	private String fileName;
//	
//	@Column(name="url",length=300,nullable=false)
//	private String url;
//	
//	@Column(name="rdate")
//	private LocalDateTime rdate;
//	
//	@Column(name="udate")
//	private LocalDateTime udate;
//	
//	@JsonBackReference
//	@ManyToOne(fetch=FetchType.LAZY)
//	@JoinColumn(name="product_id")
//	private Product product;
//}

