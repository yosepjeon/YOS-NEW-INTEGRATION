package com.yosep.msa.yosorderapi.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@EqualsAndHashCode(of="orderId")
@Table(name="yoggaebi_cart_jpa")
@Entity
@ToString
public class Cart {
	@Id
	@Column(name="order_id",length=300)
	private String orderId;
	
	@Column(name="user_name",length=30,nullable=false)
	private String userName;
	
	@Column(name="product_id",length=300,nullable=false)
	private String productId;
	
	@Column(name="rdate")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderRdate;
	
	@Column(name="udate")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderUdate;
}
