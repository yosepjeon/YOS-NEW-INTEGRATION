package com.yosep.msa.productapi.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProductTest {

	@Test
	public void builder() {
		Product product = Product.builder().productId("p1").productName("p1").productType(ProductType.TOP).productDetailType(ProductDetailType.LONGSLEEV).build();
		
		System.out.println(product.toString());
		assertThat(product).isNotNull();
	}
	
	@Test
	@DisplayName("DTO -> multiValueMap 테스트, multiValueMap -> DTO 테스트")
	public void multiValueMapTestConvertTest() {
		
		
//		ProductDTO productDTO = ProductDTO.builder();
	}
	
	@Test
	@DisplayName("ByteArrayResource -> MultipartFile Test")
	public void convertFromByteArrayResourceToMultipartFileTest() throws IOException {
//		ClassPathResource resource = new ClassPathResource("file_io_test/digitalwatch.jpg");
//		File file = resource.getFile();
//		
//		MultipartFile mockFile = new MockMultipartFile("digitalwatch.jpg", new FileInputStream(file));
//		
		
	}

}
