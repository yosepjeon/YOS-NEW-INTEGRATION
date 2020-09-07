package com.yosep.msa.productapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yosep.msa.productapi.entity.ProductProfileFile;

public interface ProductProfileFileRepository extends JpaRepository<ProductProfileFile, String> {
	
}
