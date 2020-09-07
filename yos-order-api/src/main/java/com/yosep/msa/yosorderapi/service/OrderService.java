package com.yosep.msa.yosorderapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yosep.msa.yosorderapi.entity.Order;
import com.yosep.msa.yosorderapi.entity.OrderDTO;
import com.yosep.msa.yosorderapi.entity.OrderMessageQueueAfterBillingDTO;
import com.yosep.msa.yosorderapi.entity.OrderState;
import com.yosep.msa.yosorderapi.entity.ProductMessageQueueAboutAfterBiilingProcessDTO;
import com.yosep.msa.yosorderapi.mq.ProductSender;
import com.yosep.msa.yosorderapi.repository.CartRepository;
import com.yosep.msa.yosorderapi.repository.CartRepositorySupport;
import com.yosep.msa.yosorderapi.repository.OrderRepository;
import com.yosep.msa.yosorderapi.repository.OrderRepositorySupport;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

@RefreshScope
@Service
@Transactional(readOnly = true)
public class OrderService {
	@Autowired
	EntityManager em;
	
	private final OrderRepository orderRepository;
	private final OrderRepositorySupport orderRepositorySupport;
	private final ProductSender productSender;
	private final CartRepository cartRepository;
	@Autowired
	private CartRepositorySupport cartRepositorySupport;

	@Autowired
	public OrderService(OrderRepository orderRepository, OrderRepositorySupport orderRepositorySupport,
			ProductSender productSender,CartRepository cartRepository) {
		this.orderRepository = orderRepository;
		this.orderRepositorySupport = orderRepositorySupport;
		this.productSender = productSender;
		this.cartRepository = cartRepository;
	}

	@Transactional(readOnly= false)
	public Order create(Order order) {
		order.setOrderRdate(LocalDateTime.now());
		order.setOrderUdate(LocalDateTime.now());

		while(this.orderRepository.findById(order.getOrderId()).isPresent()) {
			order.setOrderId(order.getSenderId() + "-" + order.getProductId() + UUID.randomUUID());
		}
		
		return orderRepository.save(order);
	}

	public Page<Order> getOrders(Pageable pageable) {
		Page<Order> page = orderRepository.findAll(pageable);

		System.out.println(orderRepository.findAll());
		return page;
	}

	public Optional<Order> getOrder(String id) {
		Optional<Order> order = orderRepository.findById(id);

		return order;
	}

	@Transactional(readOnly = false)
	public List<Order> changeStateBuyAllOrders(String userId) {
		List<Order> orders = orderRepository.findAll();

		orders.forEach(order -> {
			order.setBuy(true);
		});

		return orderRepository.saveAll(orders);
	}

	public List<OrderDTO> getOrdersBeforeBuyByUserId(String userId) {
		List<Order> orders = orderRepositorySupport.findOrdersBeforeBuyByUserId(userId);
		List<OrderDTO> orderDTOs = new ArrayList<>();

		orders.forEach(order -> {
			OrderDTO orderDTO = OrderDTO.builder().orderId(order.getOrderId()).productId(order.getProductId())
					.senderId(order.getSenderId()).senderName(order.getSenderName())
					.receiverName(order.getReceiverName()).phone(order.getPhone()).postCode(order.getPostCode())
					.roadAddr(order.getRoadAddr()).jibunAddr(order.getJibunAddr()).extraAddr(order.getExtraAddr())
					.detailAddr(order.getDetailAddr()).isBuy(order.isBuy()).state(OrderState.WAIT)
					.orderRdate(order.getOrderRdate()).orderUdate(order.getOrderUdate())
//					.orderRdate(new DateType(order.getOrderRdate()))
//					.orderUdate(new DateType(order.getOrderUdate()))
					.build();

			orderDTOs.add(orderDTO);
		});

		return orderDTOs;
	}

	public boolean changeProductAboutOrder(List<ProductMessageQueueAboutAfterBiilingProcessDTO> productMessageQueueAboutAfterBiilingProcessDTOList) {
		boolean isSend = productSender.sendToproductQAfterBillingChannel(productMessageQueueAboutAfterBiilingProcessDTOList);

		return isSend;
	}

	@Transactional(readOnly=false)
	public void updateOrdersAfterChangeProductStateAfterBilling(
		List<OrderMessageQueueAfterBillingDTO> orderMessageQueueAfterBillingDTOs) {
		Map<String, String> mappingOrderToProduct = new HashMap<>();
		List<OrderMessageQueueAfterBillingDTO> failOrderProductNameList = new ArrayList<>();
		orderMessageQueueAfterBillingDTOs.forEach(orderMessageQueueAfterBillingDTO -> {
			mappingOrderToProduct.put(orderMessageQueueAfterBillingDTO.getOrderId(),
					orderMessageQueueAfterBillingDTO.getProductName());
			if (orderMessageQueueAfterBillingDTO.isSuccess()) {
				System.out.println(orderMessageQueueAfterBillingDTO.getOrderId() + "!!!");
//				cartRepository.deleteById(orderMessageQueueAfterBillingDTO.getOrderId());
				cartRepositorySupport.deleteCartElementByOrderId(orderMessageQueueAfterBillingDTO.getOrderId());
				orderRepositorySupport.updateOrderProcessSuccess(orderMessageQueueAfterBillingDTO.getOrderId());
			} else {
				failOrderProductNameList.add(orderMessageQueueAfterBillingDTO);
				orderRepositorySupport.updateOrderProcessFail(orderMessageQueueAfterBillingDTO.getOrderId());
			}
			em.clear();
		});

		// TODO: 리펙토링
		String apiKey = "NCS3J86A3J8S30QP";
		String apiSecret = "7EBMGL5ZYD3DKWIQ9HS7VEXTFQSHAK5A";

		HashMap<String, String> params = new HashMap<String, String>();
		Message coolsms = new Message(apiKey, apiSecret);
		boolean isInitialize = false;
		List<Order> failOrders = orderRepositorySupport.findOrdersToSendMMS();
		int failOrdersSize = failOrders.size();

		if (failOrdersSize > 0) {
			StringBuffer sb = new StringBuffer();
			
			Order orderToInitialize = failOrders.get(0);
			params.put("to", orderToInitialize.getPhone());
			params.put("from", "010-2683-2450"); // 사전에 사이트에서 번호를 인증하고 등록하여야 함
			params.put("type", "SMS");
			params.put("app_version", "test app 1.2");
			
			sb.append("총 " + failOrdersSize + "건의 주문 실패가 있습니다.(재고 부족)\n\n");
			int count = 1;
			
			for(Order order : failOrders) {
				sb.append(count + ") " + mappingOrderToProduct.get(order.getOrderId()) + "\n");
				count++;
			}
//			failOrders.forEach(order -> {
//				sb.append(count + ") " + mappingOrderToProduct.get(order.getOrderId()) + "\n");
//			});
			
			sb.append("\n");
			sb.append("1주일 내에 환불이 진행될 예정입니다. 환불이 완료된 경우 문자로 알려드리겠습니다. 감사합니다.");
			
			params.put("text", sb.toString()); // 메시지 내용
			
			try {
				JSONObject obj = (JSONObject) coolsms.send(params);
				System.out.println(obj.toString()); // 전송 결과 출력
				failOrders.forEach(order -> {
					orderRepositorySupport.updateOrderWaitRefund(order.getOrderId());
				});
			} catch (CoolsmsException e) {
				System.out.println(e.getMessage());
				System.out.println(e.getCode());
			}
		}

	}

	public void testMessageQueue() {
		System.out.println(productSender.sendToProductQTestChannel());
	}

	public Order patchOrder() {
		return null;
	}

	public ProductSender getProductSender() {
		return productSender;
	}
}
