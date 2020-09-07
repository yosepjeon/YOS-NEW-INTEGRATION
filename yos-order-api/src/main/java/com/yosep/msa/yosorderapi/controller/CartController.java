package com.yosep.msa.yosorderapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yosep.msa.yosorderapi.common.ErrorsResource;
import com.yosep.msa.yosorderapi.entity.Cart;
import com.yosep.msa.yosorderapi.entity.CartDTO;
import com.yosep.msa.yosorderapi.entity.CartResource;
import com.yosep.msa.yosorderapi.service.CartService;

@SuppressWarnings("rawtypes")
@RestController
@CrossOrigin
@RequestMapping(value="/api/cart", produces = MediaTypes.HAL_JSON_VALUE)
public class CartController {
	private final CartService cartService;
	private final ModelMapper modelMapper;
	private WebMvcLinkBuilder controllerLinkBuilder;
	private CartResource cartResource;
	
	@Autowired
	public CartController(CartService cartService,ModelMapper modelMapper) {
		// TODO Auto-generated constructor stub
		this.cartService = cartService;
		this.modelMapper = modelMapper;
		controllerLinkBuilder = linkTo(CartController.class);
	}
	
	@GetMapping("/{userName}")
	public ResponseEntity getCartByUserName(@PathVariable("userName") String userName) {
		List<Cart> elements = cartService.getCartElementsByUserId(userName);
		
		List<EntityModel<Cart>> cartResource = elements.stream().map(e -> EntityModel.of(e)
				.add(controllerLinkBuilder.slash("{userId}").withRel("get-cart"))
				.add(controllerLinkBuilder.slash("{userId}").slash("{orderId}").withRel("patch-cart"))
				.add(controllerLinkBuilder.slash("{userId}").slash("{orderId}").withRel("delete-cart"))
				).collect(Collectors.toList());
		
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("body", cartResource);
		
		return ResponseEntity.ok(result);
	}
	
	@PostMapping
	public ResponseEntity addToCart(@RequestBody @Valid CartDTO cartDTO, Errors errors) {
		System.out.println(cartDTO.toString());
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		Cart cartElement = modelMapper.map(cartDTO, Cart.class);
		Cart savedCartElement = cartService.addElementToCart(cartElement);
		
		cartResource = new CartResource(savedCartElement);
		cartResource.add(controllerLinkBuilder.slash("{userId}").withRel("get-cart"));
		cartResource.add(controllerLinkBuilder.slash("{userId}").slash("{orderId}").withRel("patch-cart"));
		cartResource.add(controllerLinkBuilder.slash("{userId}").slash("{orderId}").withRel("delete-cart"));
		
		return ResponseEntity.created(controllerLinkBuilder.slash("{userId}").slash("{orderId}").toUri()).body(cartResource);
	}
	
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
}
