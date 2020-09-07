package com.yosep.yosuserapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yosep.yosuserapi.user.entity.User;

public interface UserRepository extends JpaRepository<User, String>{
	
	
}
