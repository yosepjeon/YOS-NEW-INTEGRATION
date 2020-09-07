package com.yosep.msa.yosorderapi.service;

import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.yosep.msa.yosorderapi.entity.Order;
import com.yosep.msa.yosorderapi.mq.ProductSender;
import com.yosep.msa.yosorderapi.repository.CartRepository;
import com.yosep.msa.yosorderapi.repository.OrderRepository;
import com.yosep.msa.yosorderapi.repository.OrderRepositorySupport;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class OrderServiceTest {

	private OrderService orderService;

	@Autowired
	private OrderService orderServiceForRabbitTest;

	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	ProductSender productSender;

	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderRepositorySupport orderRepositorySupport;

	Pageable pageable;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mockOrderRepository();

		orderService = new OrderService(orderRepository, orderRepositorySupport, productSender, cartRepository);

	}

	@Test
	@DisplayName("주문 목록을 가져오는 비즈니스 로직 테스트")
	public void getOrdersTest() {
		Page<Order> orders = orderService.getOrders(pageable);

		System.out.println(orders);
	}

	@Test
	@DisplayName("주문 상세 정보를 가져오는 비즈니스 로직 테스트")
	public void getOrderTest() {
		Optional<Order> order = orderService.getOrder("1");

		System.out.println(order.get().toString());
	}

	@Test
	@DisplayName("Message Queue 연결 테스트")
	public void messageQueueConnectTest() {
		System.out.println("#######");
		boolean result = orderServiceForRabbitTest.getProductSender().sendToProductQTestChannel();
		Assert.assertEquals(true, result);
		System.out.println(result);
		System.out.println("#######");

	}

	/*
	@Test
	@DisplayName("MMS 발송 테스트")
	public void mmsTest() {
		String apiKey = "NCS3J86A3J8S30QP";
		String apiSecret = "7EBMGL5ZYD3DKWIQ9HS7VEXTFQSHAK5A";

		Message coolsms = new Message(apiKey, apiSecret);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("to", "01026832450");
		params.put("from", "01026832450"); // 사전에 사이트에서 번호를 인증하고 등록하여야 함
		params.put("type", "SMS");
		params.put("text", "Test Message입니다."); // 메시지 내용
		params.put("app_version", "test app 1.2");

		try {
			JSONObject obj = (JSONObject) coolsms.send(params);
			System.out.println(obj.toString()); // 전송 결과 출력
		} catch (CoolsmsException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCode());
		}
	}
	*/

	private void mockOrderRepository() {
		List<Order> orders = new ArrayList<>();

		IntStream.range(0, 30).forEach(element -> {
			Order order = Order.builder().orderId(String.valueOf(element)).build();
			orders.add(order);

			given(orderRepository.findById(order.getOrderId())).willReturn(Optional.of(order));
		});

		given(orderRepository.findAll()).willReturn(orders);
	}

}
