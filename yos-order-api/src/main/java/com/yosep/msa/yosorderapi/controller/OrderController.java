package com.yosep.msa.yosorderapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.yosep.msa.yosorderapi.common.ErrorsResource;
import com.yosep.msa.yosorderapi.entity.CartDTO;
import com.yosep.msa.yosorderapi.entity.Order;
import com.yosep.msa.yosorderapi.entity.OrderDTO;
import com.yosep.msa.yosorderapi.entity.OrderResource;
import com.yosep.msa.yosorderapi.entity.OrderValidator;
import com.yosep.msa.yosorderapi.entity.ProductMessageQueueAboutAfterBiilingProcessDTO;
import com.yosep.msa.yosorderapi.exception.OrderNotFoundException;
import com.yosep.msa.yosorderapi.service.OrderService;

@RestController
@CrossOrigin
@RequestMapping(value="/api/orders", produces = MediaTypes.HAL_JSON_VALUE)
public class OrderController {
	private final OrderService orderService;
	private final ModelMapper modelMapper;
	private WebMvcLinkBuilder controllerLinkBuilder;
	private OrderResource orderResource;
	private final OrderValidator orderValidator;
	private RestTemplate restTemplate;
	
	@Autowired
	public OrderController(OrderService orderService, ModelMapper modelMapper,OrderValidator orderValidator, RestTemplate restTemplate) {
		this.orderService = orderService;
		this.modelMapper = modelMapper;
		this.orderValidator = orderValidator;
		controllerLinkBuilder = linkTo(OrderController.class);
		this.restTemplate = restTemplate;
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@GetMapping
	public ResponseEntity getOrders(Pageable pageable, PagedResourcesAssembler<Order> assembler) {
		Page<Order> page = this.orderService.getOrders(pageable);
		
		PagedModel<EntityModel<Order>> pagedResources = assembler.toModel(page, o -> {
			OrderResource orderResource = new OrderResource(o);
			orderResource.add(controllerLinkBuilder.withRel("get-orders"));
			orderResource.add(controllerLinkBuilder.slash(o.getOrderId()).withRel("get-order"));
			orderResource.add(controllerLinkBuilder.slash(o.getOrderId()).withRel("patch-order"));
			
			return orderResource;
		});
		pagedResources.add(new Link("/docs/index.html#resources-orders-list").withRel("profile"));
		
		return ResponseEntity.ok(pagedResources);
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@GetMapping("/{id}")
	public ResponseEntity getOrder(@PathVariable String id) {
		@SuppressWarnings("unused")
		URI orderURI = controllerLinkBuilder.slash(id).toUri();
		
		try {
			Optional<Order> optionalOrder = orderService.getOrder(id);
			if(!optionalOrder.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			
			Order order = optionalOrder.get();
			
			orderResource = new OrderResource(order);
			orderResource.add(controllerLinkBuilder.withRel("get-orders"));
			orderResource.add(controllerLinkBuilder.slash(id).withRel("get-product"));
			orderResource.add(controllerLinkBuilder.slash(id).withRel("patch-order"));
			orderResource.add(new Link("/docs/index.html#resources-order-get").withRel("profile"));
			
			return ResponseEntity.ok(orderResource);
		}catch(OrderNotFoundException one) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/orders-before-buy-by-userid/{userId}")
	public ResponseEntity getOrdersBeforeBuyByUserId(@PathVariable("userId")String userId) {
		List<OrderDTO> orders = this.orderService.getOrdersBeforeBuyByUserId(userId);
		
		List<EntityModel<OrderDTO>> ordersResource = orders.stream().map(order -> EntityModel.of(order)
				.add(controllerLinkBuilder.slash("orders-before-buy-by-userid").slash(userId).withRel("orders-before-buy-by-userid"))
				.add(controllerLinkBuilder.slash(order.getOrderId()).withRel("get-order"))
				.add(controllerLinkBuilder.slash(order.getOrderId()).withRel("patch-order"))
				).collect(Collectors.toList());
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("body", ordersResource);
		
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/order-list")
	public ResponseEntity createOrderList(@RequestBody List<OrderDTO> orderList) {
		List<ProductMessageQueueAboutAfterBiilingProcessDTO> productMessageQueueAboutAfterBiilingProcessDTOList = new ArrayList<>();
		System.out.println("orderList Size: " + orderList.size());
		
		orderList.forEach(o -> {
			System.out.println(o.toString());
			Order order = modelMapper.map(o, Order.class);
			Order newOrder = orderService.create(order);
			ProductMessageQueueAboutAfterBiilingProcessDTO productMessageQueueAboutAfterBiilingProcessDTO = new ProductMessageQueueAboutAfterBiilingProcessDTO(newOrder.getOrderId(),newOrder.getProductId());
			
			productMessageQueueAboutAfterBiilingProcessDTOList.add(productMessageQueueAboutAfterBiilingProcessDTO);
		});
		
		orderService.changeProductAboutOrder(productMessageQueueAboutAfterBiilingProcessDTOList);
		
		URI createdURI = controllerLinkBuilder.slash("/order-list").toUri();
		return ResponseEntity.created(createdURI).build();
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@PostMapping
	public ResponseEntity createOrder(@RequestBody @Valid OrderDTO orderDTO, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
//		orderCreateValidator.validate(orderDtoForInitialization, errors);
		
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		System.out.println("시작");
		
		Order order = modelMapper.map(orderDTO,  Order.class);
		Order newOrder = orderService.create(order);
		
		orderResource = new OrderResource(newOrder);
		orderResource.add(controllerLinkBuilder.withRel("get-products"));
		orderResource.add(controllerLinkBuilder.withRel("create-product"));
		orderResource.add(controllerLinkBuilder.slash(order.getOrderId()).withRel("get-order"));
		orderResource.add(controllerLinkBuilder.slash(order.getOrderId()).withRel("patch-order"));
		orderResource.add(new Link("/docs/index.html#resources-orders-create").withRel("profile"));
		
		URI createdURI = controllerLinkBuilder.slash(newOrder.getOrderId()).toUri();
		
		return ResponseEntity.created(createdURI).body(orderResource);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("/updatedOrderAfterBuy")
	public ResponseEntity changeOrderStateAfterBuy(@RequestBody List<CartDTO> requestBody) {
//		List<OrderDTO> orderDTOs = orderService.getOrdersBeforeBuyByUserId(userId);
//		List<ProductMessageQueueAboutAfterBiilingProcessDTO> productIdsAboutOrders = new ArrayList<>();
//		ResponseEntity re = restTemplate.getForEntity("http://localhost:8095/user/" + userId, String.class);
		
//		orderDTOs.forEach(order -> {
//			productIdsAboutOrders.add(new ProductMessageQueueAboutAfterBiilingProcessDTO(order.getOrderId(), order.getProductId()));
//		});
		System.out.println(requestBody);
		
		
		List<ProductMessageQueueAboutAfterBiilingProcessDTO> productIdsAboutOrders = new ArrayList<>();
		
		boolean isSuccess = orderService.changeProductAboutOrder(productIdsAboutOrders);
		
		if(!isSuccess) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/testMQ")
	public void testMQ() {
		orderService.testMessageQueue();
	}
	
	@GetMapping("/stream-test/{number}")
	public int streamTest(@PathVariable("number") int number) {
		try{
		    Thread.sleep(number * 1000);
		}catch(InterruptedException e){
		    e.printStackTrace();
		}
		
		return number;
	}
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
	
}
