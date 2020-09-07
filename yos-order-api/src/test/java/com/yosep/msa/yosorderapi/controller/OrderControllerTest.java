package com.yosep.msa.yosorderapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;

import com.netflix.discovery.converters.Auto;
import com.yosep.msa.yosorderapi.common.BaseControllerTest;
import com.yosep.msa.yosorderapi.common.TestDescription;
import com.yosep.msa.yosorderapi.entity.Order;
import com.yosep.msa.yosorderapi.entity.OrderDTO;
import com.yosep.msa.yosorderapi.repository.OrderRepository;
import com.yosep.msa.yosorderapi.service.OrderService;

public class OrderControllerTest extends BaseControllerTest{
	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	OrderService orderService;
	
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
	
//	@Test
//	@TestDescription("주문정보 하나를 가져오는 테스트")
//	public void getOrderTest() throws Exception {
//		System.out.println("-------- get order test --------");
//		this.mockMvc.perform(get("/api/orders/test1").header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest))
//				.contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)).andDo(print()).andExpect(status().isOk());
//		System.out.println();
//	}
	
	@Test
	@TestDescription("주문 리스트를 가져오는 테스트")
	public void getOrdersTest() throws Exception{
		System.out.println("-------- get orders test ---------");
		this.mockMvc.perform(get("/api/orders").param("page", "0").param("size", "10").param("sort", "orderRdate,DESC").header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)).andDo(print()).andExpect(status().isOk());
		System.out.println();
	}
	
	@Test
	@Rollback(value = true)
	@TestDescription("주문 생성 테스트")
	public void createorderTest() throws Exception {
		System.out.println("--------create orders Test-----------");
		OrderDTO orderDTO = OrderDTO.builder().orderId("test2").productId("test2").senderId("enekelx1").senderName("요깨비").receiverName("요깨비").phone("010-1234-1234").postCode("12345").roadAddr("abcde").jibunAddr("abcde").extraAddr("abcde").detailAddr("abcde").build();

		mockMvc.perform(post("/api/orders")
				.header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(orderDTO))).andDo(print()).andExpect(status().isCreated());
//				.andDo(document("create-order",
//						links(linkWithRel("self").description("자기 자신을 가리키는 링크로 get-order와 일맥상통합니다."),
//								linkWithRel("get-orders").description("물건 리스트를 가져오는 링크"),
//								linkWithRel("get-order").description("특정 물건을 가져오는 링크"),
//								linkWithRel("put-order").description("특정 물건의 일부 프로퍼티를 수정하는 링크"),
//								linkWithRel("profile").description("profile 링크")),
//						requestHeaders(
//								headerWithName(HttpHeaders.CONTENT_TYPE).description("application/json;charset=UTF-8"),
//								headerWithName(HttpHeaders.AUTHORIZATION)
//										.description("Bearer token-value MSA 모든 서비스를 이용하기 위해서는 해당 인증값을 반드시 넣어야 합니다."),
//								headerWithName(HttpHeaders.ACCEPT).description("accept header")),
//						requestFields(fieldWithPath("orderId").type(JsonFieldType.STRING).description("제품 아이디"),
//								fieldWithPath("orderName").type(JsonFieldType.STRING).description("제품 이름"),
//								fieldWithPath("orderSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
//								fieldWithPath("orderPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
//								fieldWithPath("orderQuantity").type(JsonFieldType.NUMBER).description("개수"),
//								fieldWithPath("orderDetail").type(JsonFieldType.STRING).description("제품 설명")),
//						responseHeaders(headerWithName(HttpHeaders.LOCATION).description("Location header"),
//								headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")),
//						responseFields(fieldWithPath("orderId").type(JsonFieldType.STRING).description("제품 아이디"),
//								fieldWithPath("orderName").type(JsonFieldType.STRING).description("제품 이름"),
//								fieldWithPath("orderSale").type(JsonFieldType.NUMBER).description("세일(소수가 아닌 정수)%"),
//								fieldWithPath("orderPrice").type(JsonFieldType.NUMBER).description("제품 가격"),
//								fieldWithPath("orderQuantity").type(JsonFieldType.NUMBER).description("개수"),
//								fieldWithPath("orderDetail").type(JsonFieldType.STRING).description("제품 설명"),
//								fieldWithPath("orderType").type(JsonFieldType.STRING).description("제품 종류"),
//								fieldWithPath("orderDetailType").type(JsonFieldType.STRING).description("제품 종류 상세"),
//								fieldWithPath("orderRdate").type(JsonFieldType.STRING).description("제품 등록 날짜"),
//								fieldWithPath("orderUdate").type(JsonFieldType.STRING).description("제품 수정 날짜"),
//								fieldWithPath("_links.self.href").description("자기자신을 가리키는 링크입니다."),
//								fieldWithPath("_links.get-orders.href").description("제품을 가져오는 링크입니다."),
//								fieldWithPath("_links.get-order.href").description("제품 리스트를 가져오는 링크입니다."),
//								fieldWithPath("_links.put-order.href").description("제품을 일부 및 전체 수정하는 링크입니다."),
//								fieldWithPath("_links.profile.href").description("REST DOC의 profile link"))));
	}
	
	@Test
	@DisplayName("order service에 유저 장바구니 가져오기.")
	public void getCartById() {
		
	}
	
	@Test
	@Rollback(value=true)
	@TestDescription("주문 완료 상태로 업데이트 해주는 테스트")
	public void changeStateBuyAllOrdersTest() {
		// given
		IntStream.range(0, 30).forEach(i -> {
			Order order = Order.builder().orderId(String.valueOf(i)).build();
			orderService.create(order);
		});
		
		// When & Then
//		this.mockMvc.perform(put(""));
	}
	
	@Test
	@TestDescription("병렬 스트림을 이용한 각종 유닛 테스트")
	public void streamTest() throws Exception {
		List<Integer> intList = Arrays.asList(1,2,3,4);
		long start,end;
		
		start = System.currentTimeMillis();
		intList.stream().forEach(n -> {
			ResultActions result = null;
			try {
				result = mockMvc.perform(get("/api/orders/stream-test/" + n)
						.header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				System.out.println(result.andReturn().getResponse().getContentAsString());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		end = System.currentTimeMillis();
		System.out.println("일반 스트림: " + (end - start)/1000.0 + "초\n");
		
		start = System.currentTimeMillis();
		intList.parallelStream().forEach(n -> {
			ResultActions result = null;
			try {
				result = mockMvc.perform(get("/api/orders/stream-test/" + n)
						.header(HttpHeaders.AUTHORIZATION, getBearerToken(accessTokenForTest)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				System.out.println(result.andReturn().getResponse().getContentAsString());
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		end = System.currentTimeMillis();
		System.out.println("병렬 스트림: " + (end - start)/1000.0 + "초");
		
		
	}
	
	public String getBearerToken(OAuth2AccessToken token) {
		return "Bearer " + token.getValue();
	}

}
