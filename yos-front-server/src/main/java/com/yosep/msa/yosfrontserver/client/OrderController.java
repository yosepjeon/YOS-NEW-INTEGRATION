package com.yosep.msa.yosfrontserver.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.yosep.msa.yosfrontserver.entity.CartDTO;
import com.yosep.msa.yosfrontserver.entity.DtoForCreatingOrder;
import com.yosep.msa.yosfrontserver.entity.OrderDTO;
import com.yosep.msa.yosfrontserver.entity.OrderState;
import com.yosep.msa.yosfrontserver.entity.Product;
import com.yosep.msa.yosfrontserver.entity.ProductProfileFile;
import com.yosep.msa.yosfrontserver.util.AuthUtil;
import com.yosep.msa.yosfrontserver.util.JsonUtil;

import net.minidev.json.JSONArray;

@CrossOrigin
@Controller
public class OrderController {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/order/{type}/{detailType}/{productId}")
	public ModelAndView showOrderPage(@PathVariable("type") String type, @PathVariable("detailType") String detailType,
			@PathVariable("productId") String productId, HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		Cookie userAuthCookie = null;
		ModelAndView mav = new ModelAndView();

		userAuthCookie = AuthUtil.getAuthCookie(request.getCookies());

		if (userAuthCookie == null) {
			System.out.println("cookie null");
			mav.setViewName("redirect:/login");
			return mav;
		}
		String token = userAuthCookie.getValue();
		Map<String, Object> tokenPayload = AuthUtil.decodeTokenPayload(token);

		ResponseEntity response = restTemplate.getForEntity("http://52.78.74.150:8075/api/products/" + productId,
				Map.class);
		LinkedHashMap<String, Object> responseBody = (LinkedHashMap<String, Object>) response.getBody();

		Product product = Product.builder().productId(String.valueOf(responseBody.get("productId")))
				.productName(String.valueOf(responseBody.get("productName")))
				.productSale(Integer.valueOf(String.valueOf(responseBody.get("productSale"))))
				.productPrice(Integer.valueOf(String.valueOf(responseBody.get("productPrice"))))
				.productQuantity(Integer.valueOf(String.valueOf(responseBody.get("productQuantity"))))
				.productDetail(String.valueOf(responseBody.get("productDetail")))
				.productProfileImageURLs((List<ProductProfileFile>) responseBody.get("productProfileImageURLs"))
				.build();

		UUID uuid = UUID.randomUUID();
		String userId = String.valueOf(tokenPayload.get("user_name"));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

		ResponseEntity getUserResponse = restTemplate.exchange("http://3.35.107.191:" + 8095 + "/user/user-name/" + userId,
				HttpMethod.GET, httpEntity, Map.class);

		if (getUserResponse.getStatusCodeValue() == 404) {
			mav.setViewName("main");
		}

		LinkedHashMap<String, Object> getUserResponseBody = (LinkedHashMap<String, Object>) getUserResponse.getBody();

		String userName = String.valueOf(getUserResponseBody.get("name"));
//		System.out.println(userName);
		
		CartDTO order = CartDTO.builder()
				.orderId(userId + "-" + productId + "-" + uuid)
				.userName(userId)
				.productId(productId)
				.build();

		headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<CartDTO> httpEntityForCreateOrder = new HttpEntity<>(order,headers);
		
		ResponseEntity addCartElementResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart", HttpMethod.POST,httpEntityForCreateOrder,Map.class);
		LinkedHashMap<String, Object> result = (LinkedHashMap<String, Object>) addCartElementResponse.getBody();

		mav.addObject("userId", userId);
		mav.addObject("product", product);
		mav.addObject(userName);
		mav.setViewName("afterAddToCart");

		return mav;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/cart")
	public ModelAndView showCartPage(HttpServletRequest request)
			throws JsonMappingException, JsonProcessingException {
		ModelAndView mav = new ModelAndView();

		Cookie AuthCookie = AuthUtil.getAuthCookie(request.getCookies());

		String token = AuthCookie.getValue();
		Map<String, Object> payload = AuthUtil.decodeTokenPayload(token);
		String userId = String.valueOf(payload.get("user_name"));

//		ResponseEntity response = OrderUtil.getOrderListBeforeBuy(userId, token, restTemplate);
//		Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
//		List<LinkedHashMap<String, Object>> orders = (List<LinkedHashMap<String, Object>>) responseBody.get("body");

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		ResponseEntity getCartResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart/" + userId, HttpMethod.GET,httpEntity,Map.class);
		Map<String, Object> getCartResponseBody = (Map<String, Object>) getCartResponse.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) getCartResponseBody.get("body");
		
		int totalPrice = 0;
		
		List<String> productIdList = new ArrayList<>();
		List<LinkedHashMap<String, Object>> products = new ArrayList<>();
		
		cartElements.forEach(e -> {
			String productId = String.valueOf(e.get("productId"));
			productIdList.add(productId);
		});
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://3.34.155.11:8070/api/products/cart")
				.queryParam("productIdList", productIdList);
		ResponseEntity getProductResponse = restTemplate.exchange(uriComponentsBuilder.toUriString() , HttpMethod.GET,httpEntity,List.class);
		List<LinkedHashMap<String, Object>> getProductResponseBody = (List<LinkedHashMap<String, Object>>) getProductResponse.getBody();

		for(LinkedHashMap<String, Object> product : getProductResponseBody) {
			products.add(product);
			totalPrice += Integer.parseInt(String.valueOf(product.get("productPrice")));
		};

		mav.addObject("products", products);
		mav.addObject("totalPrice", totalPrice);
		mav.setViewName("checkout_cart");
		return mav;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/pay")
	public ModelAndView showPayPage(HttpServletRequest request,
			@RequestParam("senderId") String senderId, @RequestParam("senderName") String senderName,
			@RequestParam("receiverName") String receiverName, @RequestParam("phone") String phone,
			@RequestParam("postCode") String postCode, @RequestParam("roadAddr") String roadAddr,
			@RequestParam("jibunAddr") String jibunAddr, @RequestParam("extraAddr") String extraAddr,
			@RequestParam("detailAddr") String detailAddr, HttpSession session) throws JsonMappingException, JsonProcessingException {
		ModelAndView mav = new ModelAndView();
		Cookie AuthCookie = AuthUtil.getAuthCookie(request.getCookies());

		String token = AuthCookie.getValue();
		Map<String, Object> payload = AuthUtil.decodeTokenPayload(token);
		String userId = String.valueOf(payload.get("user_name"));
		/*
		Order orderForPost = new Order();
		orderForPost.setReceiverName(receiverName);
		orderForPost.setPhone(phone);
		orderForPost.setPostCode(postCode);
		orderForPost.setRoadAddr(roadAddr);
		orderForPost.setJibunAddr(jibunAddr);
		orderForPost.setExtraAddr(extraAddr);
		orderForPost.setDetailAddr(detailAddr);

		ResponseEntity response = OrderUtil.getOrderListBeforeBuy(userId, token, restTemplate);
		Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
		System.out.println("responseBody: " + responseBody);
		List<LinkedHashMap<String, Object>> orders = (List<LinkedHashMap<String, Object>>) responseBody.get("body");

		List<LinkedHashMap<String, Object>> products = new ArrayList<>();
		int totalPrice = 0;
		
		for(LinkedHashMap<String,Object> orderDTO : orders) {
			// network traffic이 너무 늘어날듯.. list에 담아서 보내기로...
			ResponseEntity productResponse = restTemplate.getForEntity("http://localhost:8075/api/products/" + orderDTO.get("productId"), Map.class);
			
			LinkedHashMap<String, Object> product = (LinkedHashMap<String, Object>) productResponse.getBody();
			products.add(product);
			totalPrice += Integer.valueOf(String.valueOf(product.get("productPrice")));
		}
		*/
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		ResponseEntity getCartResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart/" + userId, HttpMethod.GET,httpEntity,Map.class);
		Map<String, Object> getCartResponseBody = (Map<String, Object>) getCartResponse.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) getCartResponseBody.get("body");
		
		List<String> productIdList = new ArrayList<>();
		List<Map<String,Object>> cart = new ArrayList<>();
		cartElements.forEach(e -> {
			Map<String,Object> map = new HashMap<>();
			String orderId = String.valueOf(e.get("orderId"));
			String userName = String.valueOf(e.get("userName"));
			String productId = String.valueOf(e.get("productId"));
			map.put("orderId", orderId);
			map.put("userName", userName);
			map.put("productId", productId);
			
			productIdList.add(productId);
			cart.add(map);
		});
		
		// cart JsonArray로 변환
		JSONArray convertedCart = JsonUtil.getJsonArrayFromList(cart);
		
		UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl("http://52.78.74.150:8075/api/products/cart")
				.queryParam("productIdList", productIdList);
		ResponseEntity getProductResponse = restTemplate.exchange(uriComponentsBuilder.toUriString() , HttpMethod.GET,httpEntity,List.class);
		List<LinkedHashMap<String, Object>> getProductResponseBody = (List<LinkedHashMap<String, Object>>) getProductResponse.getBody();

		int totalPrice = 0;
		List<LinkedHashMap<String, Object>> products = new ArrayList<>();
		
		for(LinkedHashMap<String, Object> product : getProductResponseBody) {
			totalPrice += Integer.parseInt(String.valueOf(product.get("productPrice")));
			products.add(product);
		};
		
		mav.addObject("productName", products.get(0).get("productName"));
		mav.addObject("productCount", products.size());
		mav.addObject("products", products);
		mav.addObject("cart",convertedCart);
		mav.addObject("totalPrice", totalPrice);
		mav.addObject("senderName", senderName);
		mav.addObject("receiverName",receiverName);
		mav.addObject("phone", phone);
		mav.addObject("postCode", postCode);
		mav.addObject("roadAddr", roadAddr);
		mav.addObject("jibunAddr",jibunAddr);
		mav.addObject("extraAddr",extraAddr);
		mav.addObject("detailAddr",detailAddr);
		mav.addObject("userId", userId);
		mav.setViewName("pay");

		return mav;
	}

	@RequestMapping("/checkout_address")
	public ModelAndView showCheckOutAddress(HttpSession session) {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("checkout_address");
		mav.addObject("userName", session.getAttribute("userName"));
		return mav;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/processPay", method=RequestMethod.POST)
	public ResponseEntity processPay(HttpServletRequest request,@RequestBody DtoForCreatingOrder requestBody) throws JsonMappingException, JsonProcessingException {
		Cookie AuthCookie = AuthUtil.getAuthCookie(request.getCookies());

		String token = AuthCookie.getValue();
		Map<String, Object> tokenPayload = AuthUtil.decodeTokenPayload(token);
		System.out.println(tokenPayload);
		String userId = String.valueOf(tokenPayload.get("user_name"));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);
		ResponseEntity getCartResponse = restTemplate.exchange("http://52.78.74.150:8035/api/cart/" + userId, HttpMethod.GET,httpEntity,Map.class);
		Map<String,Object> getCartResponseBody = (Map<String,Object>) getCartResponse.getBody();
		List<LinkedHashMap<String, Object>> cartElements = (List<LinkedHashMap<String, Object>>) getCartResponseBody.get("body");
		
		String senderName = requestBody.getSenderName();
		String receiverName = requestBody.getReceiverName();
		String phone = requestBody.getPhone();
		String postCode = requestBody.getPostCode();
		String roadAddr = requestBody.getRoadAddr();
		String jibunAddr = requestBody.getJibunAddr();
		String extraAddr = requestBody.getExtraAddr();
		String detailAddr = requestBody.getDetailAddr();
		
		List<OrderDTO> orderList = new ArrayList<>();
		
		cartElements.forEach(element -> {
			OrderDTO order = OrderDTO.builder()
					.orderId(String.valueOf(element.get("orderId")))
					.productId(String.valueOf(element.get("productId")))
					.senderId(String.valueOf(element.get("userName")))
					.senderName(senderName)
					.receiverName(receiverName)
					.phone(phone)
					.postCode(postCode)
					.roadAddr(roadAddr)
					.jibunAddr(jibunAddr)
					.extraAddr(extraAddr)
					.detailAddr(detailAddr)
					.isBuy(false)
					.state(OrderState.WAIT)
					.build();
			
			orderList.add(order);
		});
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<List<OrderDTO>> httpEntityForCreateOrder = new HttpEntity<>(orderList, headers);
		ResponseEntity createdOrdersResponse = restTemplate.exchange("http://52.78.74.150:8035/api/orders/order-list", HttpMethod.POST, httpEntityForCreateOrder, ResponseEntity.class);
		
//		ModelAndView mav = new ModelAndView();
		
		if(createdOrdersResponse.getStatusCode() == HttpStatus.CREATED) {
//			mav.setViewName("checkout_pay_success");
			return ResponseEntity.ok().build();
		}else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//			mav.setViewName("checkout_pay_fail");
		}
		
//		return mav;
//		List<OrderDTO> orderList = new ArrayList<>();
//		requestBody.getCart().forEach(element -> {
//			OrderDTO order = OrderDTO.builder()
//					.orderId(element.getOrderId())
//					.productId(element.getProductId())
//					.senderId(tokenPayload.get(""))
//					.build();
//		});
		
		/*
		ResponseEntity getUserResponse = restTemplate.exchange("http://localhost:" + 8095 + "/user/user-name/" + userId,
				HttpMethod.GET, httpEntity, Map.class);

		ModelAndView mav = new ModelAndView();
		ObjectMapper om = new ObjectMapper();

		List<String> productIdListIncludedOrders = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		
		ResponseEntity orderResponse = OrderUtil.getOrderListBeforeBuy(userId, token, restTemplate);
		Map<String, Object> responseBody = (Map<String, Object>) orderResponse.getBody();
		List<LinkedHashMap<String, Object>> orders = (List<LinkedHashMap<String, Object>>) responseBody.get("body");

		int totalPrice = 0;
		
		orders.forEach(order -> {
			productIdListIncludedOrders.add(String.valueOf(order.get("productId")));
		});

		mav.addObject("products", products);
		mav.addObject("totalPrice", totalPrice);
		mav.setViewName("checkout_pay_success");

		return mav;
		
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("checkout_pay_success");
		
		return mav;
		*/
	}
	
	@RequestMapping(value = "/successPay", method=RequestMethod.GET)
	public ModelAndView processPay(HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("checkout_pay_success");
		
		return mav;
	}
	
	/*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/processPay", method=RequestMethod.POST)
	public ModelAndView processPayLegacy(HttpServletRequest request,@RequestBody List<Map<String, Object>> requestBody) throws JsonMappingException, JsonProcessingException {
		
		Cookie AuthCookie = AuthUtil.getAuthCookie(request.getCookies());

		String token = AuthCookie.getValue();
		Map<String, Object> tokenPayload = AuthUtil.decodeTokenPayload(token);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

		ResponseEntity getUserResponse = restTemplate.exchange("http://localhost:" + 8095 + "/user/user-name/" + userId,
				HttpMethod.GET, httpEntity, Map.class);

		ModelAndView mav = new ModelAndView();
		ObjectMapper om = new ObjectMapper();

		List<String> productIdListIncludedOrders = new ArrayList<>();
		List<Product> products = new ArrayList<>();
		
		ResponseEntity orderResponse = OrderUtil.getOrderListBeforeBuy(userId, token, restTemplate);
		Map<String, Object> responseBody = (Map<String, Object>) orderResponse.getBody();
		List<LinkedHashMap<String, Object>> orders = (List<LinkedHashMap<String, Object>>) responseBody.get("body");

		int totalPrice = 0;
		
		orders.forEach(order -> {
			productIdListIncludedOrders.add(String.valueOf(order.get("productId")));
		});

		mav.addObject("products", products);
		mav.addObject("totalPrice", totalPrice);
		mav.setViewName("checkout_pay_success");

		return mav;
		
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("checkout_pay_success");
		
		return mav;
	}
	*/
	
	@RequestMapping(value="/processPayTest", method=RequestMethod.POST)
	public ResponseEntity processPayTest(@RequestBody DtoForCreatingOrder requestBody) {
		System.out.println(requestBody.getSenderName());
		System.out.println(requestBody.getPostCode());
		
		return ResponseEntity.ok().build();
	}
}
