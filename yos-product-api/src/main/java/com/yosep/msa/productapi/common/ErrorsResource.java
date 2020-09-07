package com.yosep.msa.productapi.common;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

public class ErrorsResource extends EntityModel<Errors>{
	@SuppressWarnings("deprecation")
	public ErrorsResource(Errors errors, Link... links) {
		super(errors, links);
		add(new Link(""));
	}
}
