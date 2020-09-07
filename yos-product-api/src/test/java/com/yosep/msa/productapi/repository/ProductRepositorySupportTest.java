package com.yosep.msa.productapi.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.productapi.entity.Product;
import com.yosep.msa.productapi.entity.ProductDetailType;
import com.yosep.msa.productapi.entity.ProductType;
import com.yosep.msa.productapi.entity.QProduct;
import com.yosep.msa.productapi.entity.QProductDescription;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductRepositorySupportTest {

	@Autowired
	ProductRepositorySupport productRepositorySupport;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	JPAQueryFactory query;
	
	@Test
	public void findAllFilteringByTypeAndDetailType() {
//		System.out.println(productRepositorySupport.findAll().toString());
//		System.out.println(productRepositorySupport.findAllFilteringByTypeAndDetailType());
	}
	
	@Test
	public void findProducts() {
		String type_str = "TOP";
		ProductType type= ProductType.valueOf(type_str);
		String detailType_str = "SHORTSLEEV";
		ProductDetailType detailType = ProductDetailType.valueOf(detailType_str);
		
		QProduct product = QProduct.product;
		QProductDescription productDescription = QProductDescription.productDescription;
		List<Product> products = query.selectFrom(product)
			.leftJoin(product.productDescriptions, productDescription)
			.fetchJoin()
			.where(product.productType.eq(type).and(product.productDetailType.eq(detailType)))
			.orderBy(product.productRdate.desc(),product.productName.asc())
			.distinct()
			.fetch();
		
		System.out.println("@@@@@");
		System.out.println(products.size());
//		products.forEach(p -> System.out.println(p));
//		System.out.println(products);
		
//		products.stream().forEach( p -> System.out.println(p));
	}

}
