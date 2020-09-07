package com.yosep.msa.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface YoggaebiUserRepository extends JpaRepository<YoggaebiUser, String>{
	Optional<YoggaebiUser> findByUserName(String id);
}
