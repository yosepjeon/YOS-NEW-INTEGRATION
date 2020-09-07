package com.yosep.msa.yosfrontserver.common;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.validation.Errors;

import com.yosep.msa.yosfrontserver.client.UserController;

public class ErrorsResource extends EntityModel<Errors>{

	@SuppressWarnings("deprecation")
	public ErrorsResource(Errors content, Link... links) {
		super(content, links);
//		add(linkTo(methodOn(UserController.class).lo).withRel("login"));
		add(linkTo(methodOn(UserController.class).showMainPage()).withRel("main_page"));
	}
	
}
