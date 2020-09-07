package com.yosep.msa.productapi.repository;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.productapi.entity.Product;
import com.yosep.msa.productapi.entity.ProductDetailType;
import com.yosep.msa.productapi.entity.ProductType;
public class ProductRepositoryTest {
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	ProductRepositorySupport productRepositorySupport;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	JPAQueryFactory query;

	@Test
	public void findOneTest() {
		@SuppressWarnings("unused")
		Product product = Product.builder()
				.productId("product1")
				.productName("product1")
				.productSale(0)
				.productPrice(210000)
				.productQuantity(100)
				.productDetail("")
				.productType(ProductType.WATCH)
				.productDetailType(ProductDetailType.DIGITAL)
				.build();
	}
	
//	@Test
//	public void queryDslTest() {
//		List<Product> productList = productRepositorySupport.findAll();
//		
//		System.out.println(productList);
//	}

}
