package com.yosep.msa.account;

//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;


public class YoggaebiUserResource extends EntityModel<YoggaebiUser>{

	public YoggaebiUserResource(YoggaebiUser user, Link... links) {
		super(user, links);
		add(linkTo(YoggaebiUserController.class).slash(user.getUserName()).withSelfRel());
		// TODO Auto-generated constructor stub
	}

}
