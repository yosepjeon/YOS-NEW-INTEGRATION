package com.yosep.msa.yosorderapi.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import com.yosep.msa.yosorderapi.controller.OrderController;

public class OrderResource extends EntityModel<Order>{
	@SuppressWarnings("deprecation")
	public OrderResource(Order order, Link...links) {
		super(order,links);
		add(linkTo(OrderController.class).slash(order.getOrderId()).withSelfRel());
	}
}
