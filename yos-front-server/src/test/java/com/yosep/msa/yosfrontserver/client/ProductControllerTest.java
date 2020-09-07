package com.yosep.msa.yosfrontserver.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.yosep.msa.yosfrontserver.common.BaseControllerTest;
import com.yosep.msa.yosfrontserver.common.TestDescription;
import com.yosep.msa.yosfrontserver.entity.Product;
import com.yosep.msa.yosfrontserver.entity.ProductDTO;

public class ProductControllerTest extends BaseControllerTest {

	ProductDTO productDTO;

	@BeforeEach
	public void setUp() {
		productDTO = ProductDTO.builder().productId("test").productName("test").productDetail("test입니다.")
				.productPrice(100000).productQuantity(100).productSale(0).build();

		resourceForTest = new ResourceOwnerPasswordResourceDetails();
		resourceForTest.setUsername("test");
		resourceForTest.setPassword("123123123");
		resourceForTest.setAccessTokenUri("http://3.35.107.191:" + 8095 + "/oauth/token");
		resourceForTest.setClientId("yoggaebi");
		resourceForTest.setClientSecret("pass");
		resourceForTest.setGrantType("password");

		clientContextForTest = new DefaultOAuth2ClientContext();

		oAuth2RestTemplateForTest = new OAuth2RestTemplate(resourceForTest, clientContextForTest);

		accessTokenForTest = oAuth2RestTemplateForTest.getAccessToken();
	}

	@Test
	@TestDescription("제품 생성 브라우저 테스트")
	public void createProductTest() {
		System.out.println(accessTokenForTest.getValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	@TestDescription("제품 페이지별 목록 보기")
	public void showProductPageTest() {
		@SuppressWarnings("rawtypes")
		ResponseEntity response = restTemplate.getForEntity("http://52.78.74.150:8075/api/products/WATCH/DIGITAL",
				Map.class);

		LinkedHashMap<String, Object> responseBody = (LinkedHashMap<String, Object>) response.getBody();
		LinkedHashMap<String, Object> embedded = (LinkedHashMap<String, Object>) responseBody.get("_embedded");

		List<Product> productsRow = new ArrayList<>();
//		List<Product> products = objectMapper.convertValue(embedded.get("productList"), new TypeReference<List<Product>>() {});
		List<EntityModel<Product>> productEntityModelList = objectMapper.convertValue(embedded.get("productList"),
				new TypeReference<List<EntityModel<Product>>>() {
				});
		
		List<Product> products = new ArrayList<>();
		
		productEntityModelList.forEach(element -> {
			products.add(element.getContent());
		});
		
		products.forEach(p -> {
			System.out.println(p.toString());
		});
	}
	
	@Test
	@DisplayName("상품 아이디 리스트를 받아와서 해당 상품들을 가져오는 테스트")
	public void getProductsByProductIdListTest() throws Exception {
		List<Integer> intStream = Arrays.asList(1,2,3,4,5);
		List<String> productIdList = new ArrayList<>();
				intStream.forEach(i -> {
					productIdList.add("test" + i);
				});
				
//		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
		HttpHeaders headers = new HttpHeaders();
//		
//		parameters.add("productIdList", productIdList);
//		System.out.println(parameters);
//		
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(headers);
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://3.34.155.11:8070/api/products/target")
				.queryParam("productIdList", productIdList);
				
		@SuppressWarnings("rawtypes")
		ResponseEntity response = restTemplate.exchange(uriComponentsBuilder.toUriString(), HttpMethod.GET,httpEntity, Map.class);
	}

	@Test
	@TestDescription("제품 상세 조회 테스트")
	public void showProductDetailPageTest() {

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	@TestDescription("장바구니에 담겨있는 상품목록들 가져오기 테스트")
	public void getProductsAboutCart() {
		List<String> productIdList = Arrays.asList("test1","test2","test3");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(headers);
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://3.34.155.11:8070/api/products/cart")
				.queryParam("productIdList", productIdList);
		
		ResponseEntity response = restTemplate.exchange(uriComponentsBuilder.toUriString() , HttpMethod.GET,httpEntity,List.class);
		List<LinkedHashMap<String, Object>> responseBody = (List<LinkedHashMap<String, Object>>) response.getBody();
		
		System.out.println(responseBody.get(0).get("productId"));
	}

}
