package com.yosep.msa.yosfrontserver.entity;

import lombok.Data;

@Data
public class ProductProfileFile {
	private String fileId;
	private String url;
//	private LocalDateTime rdate;
//	private LocalDateTime udate;
	private String rdate;
	private String udate;
}
