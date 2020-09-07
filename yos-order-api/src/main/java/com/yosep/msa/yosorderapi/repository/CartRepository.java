package com.yosep.msa.yosorderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yosep.msa.yosorderapi.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, String>{

}
