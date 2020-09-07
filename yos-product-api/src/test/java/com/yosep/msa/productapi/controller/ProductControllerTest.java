package com.yosep.msa.productapi.controller;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.converters.Auto;
import com.yosep.msa.productapi.common.BaseControllerTest;
import com.yosep.msa.productapi.entity.ProductDTO;
import com.yosep.msa.productapi.repository.ProductRepository;
import com.yosep.msa.productapi.service.ProductService;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProductControllerTest extends BaseControllerTest {
	@Autowired
	ProductRepository productRepository;

	@Autowired
	ProductService productService;

	@Autowired
	RestTemplate restTemplate;

	@Auto
	ResourceOwnerPasswordResourceDetails resourceForTest;

	OAuth2AccessToken accessTokenForTest;
	DefaultOAuth2ClientContext clientContextForTest;
	OAuth2RestTemplate oAuth2RestTemplateForTest;

	@Before
	public void setup() {
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
	@DisplayName(value="제품 하나를 가져오는 테스트")
	public void getProductTest() throws Exception {
		System.out.println("--------get Product Test-----------");
		mockMvc.perform(get("/api/products/test1")).andDo(print())
		.andDo(document("get-product",
				links(linkWithRel("self").description("자기 자신을 가리키는 링크로 get-product와 일맥상통합니다."),
						linkWithRel("get-products").description("물건 리스트를 가져오는 링크"),
						linkWithRel("get-product").description("특정 물건을 가져오는 링크"),
						linkWithRel("patch-product").description("특정 물건의 일부 프로퍼티를 수정하는 링크"),
						linkWithRel("profile").description("profile 링크")),
				requestHeaders(
//						headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json;charset=UTF-8"),
//						headerWithName(HttpHeaders.AUTHORIZATION)
//								.description("Bearer token-value MSA 모든 서비스를 이용하기 위해서는 해당 인증값을 반드시 넣어야 합니다."),
//						headerWithName(HttpHeaders.ACCEPT).description("accept header")
						),
				responseHeaders(
//						headerWithName(HttpHeaders.LOCATION).description("Location header"),
						headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")),
				responseFields(
						fieldWithPath("productId").type(JsonFieldType.STRING).description("제품 아이디"),
						fieldWithPath("productName").type(JsonFieldType.STRING).description("제품 이름"),
						fieldWithPath("productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
						fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
						fieldWithPath("productQuantity").type(JsonFieldType.NUMBER).description("개수"),
						fieldWithPath("productDetail").type(JsonFieldType.STRING).description("제품 설명"),
						fieldWithPath("productType").type(JsonFieldType.STRING).description("제품 종류"),
						fieldWithPath("productDetailType").type(JsonFieldType.STRING).description("제품 종류 상세"),
						fieldWithPath("productRdate").type(JsonFieldType.STRING).description("제품 등록 날짜"),
						fieldWithPath("productUdate").type(JsonFieldType.STRING).description("제품 수정 날짜"),
//						fieldWithPath("productProfileImageURL").type(JsonFieldType.STRING).description("제품 프로필 이미지 URL"),
						fieldWithPath("productDescriptions").type(JsonFieldType.ARRAY).description("제품 설명"),
						fieldWithPath("productProfileImageURLs[].fileId").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 아이디"),
						fieldWithPath("productProfileImageURLs[].url").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 URL"),
						fieldWithPath("productProfileImageURLs[].rdate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 등록날짜"),
						fieldWithPath("productProfileImageURLs[].udate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 수정날짜"),
						fieldWithPath("_links.self.href").description("자기자신을 가리키는 링크입니다."),
						fieldWithPath("_links.get-products.href").description("제품을 가져오는 링크입니다."),
						fieldWithPath("_links.get-product.href").description("제품 리스트를 가져오는 링크입니다."),
						fieldWithPath("_links.patch-product.href").description("제품을 일부 및 전체 수정하는 링크입니다."),
						fieldWithPath("_links.profile.href").description("REST DOC의 profile link")
						
						)));;
		System.out.println();
	}

	@Test
	@DisplayName(value="제품 리스트를 가져오는 테스트")
	public void getProductsTest() throws Exception {
		System.out.println("--------get Products Test-----------");
		this.mockMvc
				.perform(get("/api/products").param("page", "0").param("size", "10").param("sort", "productRdate,DESC"))
				.andDo(print()).andExpect(status().isOk())
				.andDo(document("get-products",
						links(linkWithRel("self").description("자기 자신을 가리키는 링크로 get-product와 일맥상통합니다."),
//								linkWithRel("get-products").description("물건 리스트를 가져오는 링크"),
//								linkWithRel("get-product").description("특정 물건을 가져오는 링크"),
//								linkWithRel("patch-product").description("특정 물건의 일부 프로퍼티를 수정하는 링크"),
								linkWithRel("profile").description("profile 링크")),
						requestHeaders(
//								headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json;charset=UTF-8"),
//								headerWithName(HttpHeaders.AUTHORIZATION)
//										.description("Bearer token-value MSA 모든 서비스를 이용하기 위해서는 해당 인증값을 반드시 넣어야 합니다."),
//								headerWithName(HttpHeaders.ACCEPT).description("accept header")
								),
						responseHeaders(
//								headerWithName(HttpHeaders.LOCATION).description("Location header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")),
						responseFields(
//								fieldWithPath("_embedded").type(JsonFieldType.OBJECT).description(""),
//								fieldWithPath("_links").type(JsonFieldType.OBJECT),
//								fieldWithPath("page").type(JsonFieldType.OBJECT)
								fieldWithPath("_embedded.productList[].productId").type(JsonFieldType.STRING).description("제품 아이디"),
								fieldWithPath("_embedded.productList[].productName").type(JsonFieldType.STRING).description("제품 이름"),
								fieldWithPath("_embedded.productList[].productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
								fieldWithPath("_embedded.productList[].productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
								fieldWithPath("_embedded.productList[].productQuantity").type(JsonFieldType.NUMBER).description("개수"),
								fieldWithPath("_embedded.productList[].productDetail").type(JsonFieldType.STRING).description("제품 설명"),
								fieldWithPath("_embedded.productList[].productType").type(JsonFieldType.STRING).description("제품 종류"),
								fieldWithPath("_embedded.productList[].productDetailType").type(JsonFieldType.STRING).description("제품 종류 상세"),
								fieldWithPath("_embedded.productList[].productRdate").type(JsonFieldType.STRING).description("제품 등록 날짜"),
								fieldWithPath("_embedded.productList[].productUdate").type(JsonFieldType.STRING).description("제품 수정 날짜"),
								subsectionWithPath("_embedded.productList[].productDescriptions[]").type(JsonFieldType.ARRAY).description("제품 설명"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].fileId").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 아이디"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].url").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 URL"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].rdate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 등록날짜"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].udate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 수정날짜"),
								fieldWithPath("_embedded.productList[]._links.self.href").type(JsonFieldType.STRING).description("개별 상품 요소를 가리키는 링크입니다."),
								fieldWithPath("_embedded.productList[]._links.get-products.href").type(JsonFieldType.STRING).description("제품을 가져오는 링크입니다."),
								fieldWithPath("_embedded.productList[]._links.get-product.href").type(JsonFieldType.STRING).description("제품 리스트를 가져오는 링크입니다."),
								fieldWithPath("_embedded.productList[]._links.patch-product.href").type(JsonFieldType.STRING).description("제품을 일부 및 전체 수정하는 링크입니다."),
								fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("상품 리스트를 가리키는 링크입니다."),
								fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("상품 리스트에 대한 REST DOC 주소 입니다."),
								fieldWithPath("page.size").type(JsonFieldType.NUMBER).description("한 페이지당 가져오는 상품 개수"),
								fieldWithPath("page.totalElements").type(JsonFieldType.NUMBER).description("전체 상품 수"),
								fieldWithPath("page.totalPages").type(JsonFieldType.NUMBER).description("총 상품 페이지 수"),
								fieldWithPath("page.number").type(JsonFieldType.NUMBER).description("현재 페이지 위치")
								)));

		System.out.println();
	}
	
	@Test
	@DisplayName(value="제품 리스트를 가져오는 테스트")
	public void getProductsFilteredByTypeAndDetailTypeTest() throws Exception {
		System.out.println("--------get Products DSL By Type And DetailType Test-----------");
		this.mockMvc
				.perform(get("/api/products/WATCH/DIGITAL").param("page", "0").param("size", "10").param("sort", "productRdate,DESC"))
				.andDo(print()).andExpect(status().isOk())
				.andDo(document("get-products-filterByType",
						links(linkWithRel("self").description("자기 자신을 가리키는 링크로 get-product와 일맥상통합니다."),
//								linkWithRel("get-products").description("물건 리스트를 가져오는 링크"),
//								linkWithRel("get-product").description("특정 물건을 가져오는 링크"),
//								linkWithRel("patch-product").description("특정 물건의 일부 프로퍼티를 수정하는 링크"),
								linkWithRel("profile").description("profile 링크")),
						requestHeaders(
								),
						responseHeaders(
//								headerWithName(HttpHeaders.LOCATION).description("Location header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")),
						responseFields(
								fieldWithPath("_embedded.productList[].productId").type(JsonFieldType.STRING).description("제품 아이디"),
								fieldWithPath("_embedded.productList[].productName").type(JsonFieldType.STRING).description("제품 이름"),
								fieldWithPath("_embedded.productList[].productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
								fieldWithPath("_embedded.productList[].productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
								fieldWithPath("_embedded.productList[].productQuantity").type(JsonFieldType.NUMBER).description("개수"),
								fieldWithPath("_embedded.productList[].productDetail").type(JsonFieldType.STRING).description("제품 설명"),
								fieldWithPath("_embedded.productList[].productType").type(JsonFieldType.STRING).description("제품 종류"),
								fieldWithPath("_embedded.productList[].productDetailType").type(JsonFieldType.STRING).description("제품 종류 상세"),
								fieldWithPath("_embedded.productList[].productRdate").type(JsonFieldType.STRING).description("제품 등록 날짜"),
								fieldWithPath("_embedded.productList[].productUdate").type(JsonFieldType.STRING).description("제품 수정 날짜"),
								subsectionWithPath("_embedded.productList[].productDescriptions[]").type(JsonFieldType.ARRAY).description("제품 설명"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].fileId").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 아이디"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].url").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 URL"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].rdate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 등록날짜"),
								fieldWithPath("_embedded.productList[].productProfileImageURLs[].udate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 수정날짜"),
								fieldWithPath("_embedded.productList[]._links.self.href").type(JsonFieldType.STRING).description("개별 상품 요소를 가리키는 링크입니다."),
								fieldWithPath("_embedded.productList[]._links.get-products.href").type(JsonFieldType.STRING).description("제품을 가져오는 링크입니다."),
								fieldWithPath("_embedded.productList[]._links.get-product.href").type(JsonFieldType.STRING).description("제품 리스트를 가져오는 링크입니다."),
								fieldWithPath("_embedded.productList[]._links.patch-product.href").type(JsonFieldType.STRING).description("제품을 일부 및 전체 수정하는 링크입니다."),
								fieldWithPath("_links.self.href").type(JsonFieldType.STRING).description("상품 리스트를 가리키는 링크입니다."),
								fieldWithPath("_links.profile.href").type(JsonFieldType.STRING).description("상품 리스트에 대한 REST DOC 주소 입니다."),
								fieldWithPath("page.size").type(JsonFieldType.NUMBER).description("한 페이지당 가져오는 상품 개수"),
								fieldWithPath("page.totalElements").type(JsonFieldType.NUMBER).description("전체 상품 수"),
								fieldWithPath("page.totalPages").type(JsonFieldType.NUMBER).description("총 상품 페이지 수"),
								fieldWithPath("page.number").type(JsonFieldType.NUMBER).description("현재 페이지 위치")
								)));

		System.out.println();
	}

	@Test
	@Rollback(value = true)
	@DisplayName(value="제품 생성 테스트")
	public void createProductTest() throws Exception {
		System.out.println("--------create Products Test-----------");
		List<String> productProfileImageURLs = new ArrayList<>();
		productProfileImageURLs.add("test1URL");
		productProfileImageURLs.add("test2URL");
		
		ProductDTO productDTO = ProductDTO.builder().productId("test-p1").productName("test-p1").productPrice(50000)
				.productQuantity(100).productSale(0).productDetail("test입니다.").productProfileImageURLs(productProfileImageURLs).build();

		mockMvc.perform(post("/api/products/TOP/LONGSLEEV/create").with(csrf())
				.header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(productDTO))).andDo(print()).andExpect(status().isCreated())
				.andDo(document("create-product",
						links(linkWithRel("self").description("자기 자신을 가리키는 링크로 get-product와 일맥상통합니다."),
								linkWithRel("get-products").description("물건 리스트를 가져오는 링크"),
								linkWithRel("get-product").description("특정 물건을 가져오는 링크"),
								linkWithRel("patch-product").description("특정 물건의 일부 프로퍼티를 수정하는 링크"),
								linkWithRel("profile").description("profile 링크")),
						requestHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json;charset=UTF-8"),
								headerWithName(HttpHeaders.AUTHORIZATION)
										.description("Bearer token-value MSA 모든 서비스를 이용하기 위해서는 해당 인증값을 반드시 넣어야 합니다."),
								headerWithName(HttpHeaders.ACCEPT).description("accept header")),
						requestFields(fieldWithPath("productId").type(JsonFieldType.STRING).description("제품 아이디"),
								fieldWithPath("productName").type(JsonFieldType.STRING).description("제품 이름"),
								fieldWithPath("productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
								fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
								fieldWithPath("productQuantity").type(JsonFieldType.NUMBER).description("개수"),
								fieldWithPath("productDetail").type(JsonFieldType.STRING).description("제품 설명"),
								fieldWithPath("productProfileImageURLs").type(JsonFieldType.ARRAY).description("제품 프로필 이미지 URL")),
						responseHeaders(headerWithName(HttpHeaders.LOCATION).description("Location header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")),
						responseFields(fieldWithPath("productId").type(JsonFieldType.STRING).description("제품 아이디"),
								fieldWithPath("productName").type(JsonFieldType.STRING).description("제품 이름"),
								fieldWithPath("productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
								fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
								fieldWithPath("productQuantity").type(JsonFieldType.NUMBER).description("개수"),
								fieldWithPath("productDetail").type(JsonFieldType.STRING).description("제품 설명"),
								fieldWithPath("productType").type(JsonFieldType.STRING).description("제품 종류"),
								fieldWithPath("productDetailType").type(JsonFieldType.STRING).description("제품 종류 상세"),
								fieldWithPath("productRdate").type(JsonFieldType.STRING).description("제품 등록 날짜"),
								fieldWithPath("productUdate").type(JsonFieldType.STRING).description("제품 수정 날짜"),
								fieldWithPath("productProfileImageURLs[].fileId").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 아이디"),
								fieldWithPath("productProfileImageURLs[].url").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 URL"),
								fieldWithPath("productProfileImageURLs[].rdate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 등록날짜"),
								fieldWithPath("productProfileImageURLs[].udate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 수정날짜"),
								fieldWithPath("productDescriptions").type(JsonFieldType.ARRAY).description("제품 설명"),
								fieldWithPath("_links.self.href").description("자기자신을 가리키는 링크입니다."),
								fieldWithPath("_links.get-products.href").description("제품을 가져오는 링크입니다."),
								fieldWithPath("_links.get-product.href").description("제품 리스트를 가져오는 링크입니다."),
								fieldWithPath("_links.patch-product.href").description("제품을 일부 및 전체 수정하는 링크입니다."),
								fieldWithPath("_links.profile.href").description("REST DOC의 profile link"))));
	}

	@Test
	@Rollback(value = false)
	@DisplayName("제품 정보 수정테스트")
	public void patchProduct() throws Exception {
		System.out.println("--------- patch product --------------");
		List<String> productProfileImageURLs = new ArrayList<>();
		ProductDTO productDTO = ProductDTO.builder().productId("test2").productName("test2").productPrice(30000).productSale(0).productQuantity(100).productDetail("Hi").productProfileImageURLs(productProfileImageURLs)
				.build();

		this.mockMvc
				.perform(patch("/api/products/{id}", productDTO.getProductId())
						.header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
						.content(this.objectMapper.writeValueAsString(productDTO)))
				.andDo(print()).andExpect(status().isOk())
				.andDo(document("patch-product",
						links(linkWithRel("self").description("자기 자신을 가리키는 링크로 get-product와 일맥상통합니다."),
								linkWithRel("get-products").description("물건 리스트를 가져오는 링크"),
								linkWithRel("get-product").description("특정 물건을 가져오는 링크"),
								linkWithRel("patch-product").description("특정 물건의 일부 프로퍼티를 수정하는 링크"),
								linkWithRel("profile").description("profile 링크")),
						requestHeaders(headerWithName(HttpHeaders.ACCEPT).description("accept header"),
								headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
								headerWithName(HttpHeaders.AUTHORIZATION)
										.description("Bearer token-value MSA 모든 서비스를 이용하기 위해서는 해당 인증값을 반드시 넣어야 합니다.")),
						requestFields(fieldWithPath("productId").type(JsonFieldType.STRING).description("제품 아이디"),
								fieldWithPath("productName").type(JsonFieldType.STRING).description("제품 이름"),
								fieldWithPath("productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
								fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
								fieldWithPath("productQuantity").type(JsonFieldType.NUMBER).description("개수"),
								fieldWithPath("productDetail").type(JsonFieldType.STRING).description("제품 설명"),
								fieldWithPath("productProfileImageURLs").type(JsonFieldType.ARRAY).description("제품 프로필 이미지 URL")),
						responseHeaders(
								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")),
						responseFields(fieldWithPath("productId").type(JsonFieldType.STRING).description("제품 아이디"),
								fieldWithPath("productName").type(JsonFieldType.STRING).description("제품 이름"),
								fieldWithPath("productSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
								fieldWithPath("productPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
								fieldWithPath("productQuantity").type(JsonFieldType.NUMBER).description("개수"),
								fieldWithPath("productDetail").type(JsonFieldType.STRING).description("제품 설명"),
								fieldWithPath("productType").type(JsonFieldType.STRING).description("제품 종류"),
								fieldWithPath("productDetailType").type(JsonFieldType.STRING).description("제품 종류 상세"),
								fieldWithPath("productRdate").type(JsonFieldType.STRING).description("제품 등록 날짜"),
								fieldWithPath("productUdate").type(JsonFieldType.STRING).description("제품 수정 날짜"),
								fieldWithPath("productDescriptions").type(JsonFieldType.ARRAY).description("제품 설명"),
								fieldWithPath("productProfileImageURLs[].fileId").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 아이디"),
								fieldWithPath("productProfileImageURLs[].url").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 URL"),
								fieldWithPath("productProfileImageURLs[].rdate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 등록날짜"),
								fieldWithPath("productProfileImageURLs[].udate").type(JsonFieldType.STRING).description("제품 프로필 이미지 요소 내 수정날짜"),
								fieldWithPath("_links.self.href").description("자기자신을 가리키는 링크입니다."),
								fieldWithPath("_links.get-products.href").description("제품을 가져오는 링크입니다."),
								fieldWithPath("_links.get-product.href").description("제품 리스트를 가져오는 링크입니다."),
								fieldWithPath("_links.patch-product.href").description("제품을 일부 및 전체 수정하는 링크입니다."),
								fieldWithPath("_links.profile.href").description("REST DOC의 profile link"))));

		System.out.println();
	}

	@Test
	@DisplayName(value="없는 상품을 조회했을 때 404 응답받기")
	public void getProduct404() throws Exception {

	}
	
	@Test
	@DisplayName(value="querydsl 테스트용")
	public void getProductsDslTest() throws Exception {
		this.mockMvc.perform(get("/api/products/dsl")).andDo(print());
	}

//	@Test
//	public void asdf() {
//		System.out.println("*******************************");
//		System.out.println(accessTokenForTest);
//	}

//	@Test
//	@TestDescription("Restdocs Set을 위한 테스트")
//	public void restDocsTestMethod() throws Exception{
//		mockMvc.perform(get("/api/product/test"))
//		.andDo(print()).andExpect(status().isOk());
////		.andDo(document("restDoc-product-test", null));
//	}

	public String getBearerToken(OAuth2AccessToken token) {
		return "Bearer " + token.getValue();
	}
}
