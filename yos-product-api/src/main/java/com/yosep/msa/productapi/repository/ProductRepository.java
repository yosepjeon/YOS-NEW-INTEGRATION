package com.yosep.msa.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yosep.msa.productapi.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

}
