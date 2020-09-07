package com.yosep.msa.account;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="yoggaebi_user_jpa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoggaebiUser {
//	@Id
//	@GeneratedValue(strategy=GenerationType.IDENTITY)
//	private Long id;

	@Id
	@Column(length = 30)
	private String userName;

	@Column(length = 100, nullable = false)
	private String password;
	
	@Column(length=50, nullable=false)
	private String name;

	@Column(length=100, nullable=false)
	private String email;
	
	@Column(length=50, nullable=false)
	private String phone;
	
	@Column(length=50, nullable=false)
	private String postCode;
	
	@Column(length=50, nullable=false)
	private String roadAddr;
	
	@Column(length=50, nullable=false)
	private String jibunAddr;
	
	@Column(length=50, nullable=false)
	private String extraAddr;
	
	@Column(length=50, nullable=false)
	private String detailAddr;
	
	@Column
	private LocalDateTime userRdate;
	
	@Column
	private LocalDateTime userUdate;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@Enumerated(value=EnumType.STRING)
	private Set<YoggaebiUserRole> roles;
}
