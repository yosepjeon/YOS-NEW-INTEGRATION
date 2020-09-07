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
@Table(name="yoggaebi_order_jpa")
@Entity
@ToString
public class Order {
	@Id
	@Column(name="order_id",length=300)
	private String orderId;
	
	@Column(name="product_id",length=300,nullable=false)
	private String productId;
	
	@Column(name="sender_id", length=50, nullable=false)
	private String senderId;
	
	@Column(name="sender_name", length=50, nullable=false)
	private String senderName;
	
	@Column(name="receiver_name",length=50, nullable=false)
	private String receiverName;
	
	@Column(name="phone",length=50, nullable=false)
	private String phone;
	
	@Column(name="post_code", length=50, nullable=false)
	private String postCode;
	
	@Column(name="road_addr", length=50, nullable=false)
	private String roadAddr;
	
	@Column(name="jibun_addr", length=50, nullable=false)
	private String jibunAddr;
	
	@Column(name="extra_addr", length=50, nullable=false)
	private String extraAddr;
	
	@Column(name="detail_addr", length=50, nullable=true)
	private String detailAddr;
	
	@Column(name="is_buy")
	private boolean isBuy;
	
	@Column(name="state")
	private int state;
	
	@Column(name="order_rdate")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderRdate;
	
	@Column(name="order_udate")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime orderUdate;
}
