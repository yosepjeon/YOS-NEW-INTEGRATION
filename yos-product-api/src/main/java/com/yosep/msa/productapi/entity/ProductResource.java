package com.yosep.msa.productapi.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import com.yosep.msa.productapi.controller.ProductController;

public class ProductResource extends EntityModel<Product>{
	@SuppressWarnings("deprecation")
	public ProductResource(Product product, Link... links) {
		super(product, links);
		add(linkTo(ProductController.class).slash(product.getProductId()).withSelfRel());
	}
}
