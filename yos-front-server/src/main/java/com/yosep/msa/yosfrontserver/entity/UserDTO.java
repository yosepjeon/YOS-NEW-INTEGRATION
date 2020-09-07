package com.yosep.msa.yosfrontserver.entity;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	@NotBlank
	@Size(max=30)
	private String userName;
	
	@NotBlank
	@Size(min=8,max=100)
	private String password;
	
	@NotBlank
	@Size(min=1,max=50)
	private String name;
	
	@NotBlank
	@Email
	@Size(max=100)
	private String email;
	
	@NotBlank
	@Size(max=50)
	private String phone;
	
	@NotNull
	@Size(max=50)
	private String postCode;
	
	@NotNull
	@Size(max=50)
	private String roadAddr;
	
	@NotNull
	@Size(max=50)
	private String jibunAddr;
	
	@NotNull
	@Size(max=50)
	private String extraAddr;
	
	@NotNull
	@Size(max=50)
	private String detailAddr;
}
