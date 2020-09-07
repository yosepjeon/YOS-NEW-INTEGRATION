package com.yosep.msa.account;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping(value="/user",produces=MediaTypes.HAL_JSON_VALUE)
public class YoggaebiUserController {
	static final String NOT_DUPLICATION_USERID = "true";
	static final String DUPLICATION_USERID = "false"; 
	
	private final YoggaebiUserService userService;
	private final ModelMapper modelMapper;
	// private final UserValicator userValidator;
	
	@Autowired
	public YoggaebiUserController(YoggaebiUserService userService,ModelMapper modelMapper) {
		this.userService = userService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/test")
	public String test() {
		return "test";
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/checkdupid")
	public ResponseEntity checkDupId(@RequestParam("userName")String userName) {
		System.out.println("call");
		
		Optional<YoggaebiUser> user = userService.findUserByUsername(userName);
		
		return user.isPresent()==true?ResponseEntity.ok(NOT_DUPLICATION_USERID):ResponseEntity.ok(DUPLICATION_USERID);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/user-name/{userId}")
	public ResponseEntity getUserNameByUserId(@PathVariable("userId") String userName) {
		System.out.println("call");
		Optional<YoggaebiUser> user = userService.findUserByUsername(userName);
		
		return user.isPresent() ? ResponseEntity.ok(user.get()):ResponseEntity.notFound().build();
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/{userId}")
	public ResponseEntity getUserByUserId(@PathVariable("userId") String userName) {
		Optional<YoggaebiUser> user = userService.findUserByUsername(userName);
		
		return ResponseEntity.ok(user.get());
	}
	
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/register")
	ResponseEntity register(@RequestBody @Valid YoggaebiUserDTO userDTO,Errors errors) {
		System.out.println("!!!");
		if(errors.hasErrors()) {
			System.out.println(errors.toString());
			return badRequest(errors);
		}
		
		// TODO: 유저 validation 구현 예정
		// *******
		
		YoggaebiUser user = modelMapper.map(userDTO, YoggaebiUser.class);
		YoggaebiUser newUser = this.userService.saveAccount(user);
		
		WebMvcLinkBuilder selfLinkBuilder = linkTo(YoggaebiUserController.class).slash(newUser.getUserName());
		
		URI createdUri = selfLinkBuilder.toUri();
		YoggaebiUserResource userResource = new YoggaebiUserResource(user);
		userResource.add(linkTo(YoggaebiUserController.class).withRel("query-events"));
		userResource.add(selfLinkBuilder.withRel("update-event"));
		userResource.add(new Link("/docs/index.html#resources-users-create").withRel("profile"));
		return ResponseEntity.created(createdUri).body(userResource);
	}

	@SuppressWarnings("rawtypes")
	private ResponseEntity badRequest(Errors errors) {
		// TODO Auto-generated method stub
		return ResponseEntity.badRequest().body(errors);
	}
	
}
