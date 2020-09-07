package com.yosep.yosuserapi.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
	
	@GetMapping("/test")
	public String testUser() {
		return "test user";
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/checkAuth")
	public ResponseEntity checkUser(){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("auth-result", "인증이 완료되었습니다.");
		ResponseEntity response = ResponseEntity.ok(result);
		
		return response;
	}
}
