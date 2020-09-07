package com.yosep.msa.yosfrontserver.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import com.yosep.msa.yosfrontserver.client.UserController;

public class UserResource extends EntityModel<User>{

	@SuppressWarnings("deprecation")
	public UserResource(User user, Link... links) {
		super(user, links);
		add(linkTo(UserController.class).slash(user.getUserName()).withSelfRel());
		// TODO Auto-generated constructor stub
	}

}
