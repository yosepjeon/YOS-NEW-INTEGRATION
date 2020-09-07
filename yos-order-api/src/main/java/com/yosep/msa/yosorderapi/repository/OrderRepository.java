package com.yosep.msa.yosorderapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yosep.msa.yosorderapi.entity.Order;

public interface OrderRepository extends JpaRepository<Order, String>{

}
