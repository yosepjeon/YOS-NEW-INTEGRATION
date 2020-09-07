package com.yosep.msa.yosorderapi.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import com.yosep.msa.yosorderapi.controller.OrderController;


public class CartResource extends EntityModel<Cart>{
	@SuppressWarnings("deprecation")
	public CartResource(Cart cart, Link... links) {
		super(cart,links);
		add(linkTo(OrderController.class).slash("cart").withSelfRel());
	}
}
