package com.yosep.msa.productapi.repository;

//import static com.yosep.msa.productapi.entity.QProduct.product;
//import static com.yosep.msa.productapi.entity.QProductDescription.productDescription;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.productapi.entity.Product;
import com.yosep.msa.productapi.entity.ProductDetailType;
import com.yosep.msa.productapi.entity.ProductType;
import com.yosep.msa.productapi.entity.QProduct;
import com.yosep.msa.productapi.entity.QProductDescription;

@Repository
public class ProductRepositorySupport extends QuerydslRepositorySupport{
	@Autowired
	EntityManager em;
	
	private final JPAQueryFactory queryFactory;
	QProduct product = QProduct.product;
	QProductDescription productDescription = QProductDescription.productDescription;
	
	public ProductRepositorySupport(JPAQueryFactory jpaQueryFactory) {
		super(Product.class);
		this.queryFactory = jpaQueryFactory;
	}
	
	public List<Product> findAll() {
		QProduct product = QProduct.product;
		QProductDescription productDescription = QProductDescription.productDescription;
//		return null;
//		return queryFactory.selectFrom(product).fetch();
		return queryFactory.selectFrom(product)
				.leftJoin(product.productDescriptions,productDescription)
				.fetchJoin()
				.orderBy(product.productRdate.desc(),product.productName.asc())
				.distinct()
				.fetch();
	}
	
	public Page<Product> findAllFilteringByTypeAndDetailType(Pageable pageable, String productType, String productDetailType) {
		System.out.println("@@@@@@@@@@");
		System.out.println(productType + "/" + productDetailType);
		
		ProductType type= ProductType.valueOf(productType);
		ProductDetailType detailType = ProductDetailType.valueOf(productDetailType);
		
//		QProduct product = QProduct.product;
//		QProductDescription productDescription = QProductDescription.productDescription;

		QueryResults<Product> productList =  queryFactory.selectFrom(product)
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.leftJoin(product.productDescriptions,productDescription)
				.fetchJoin()
				.where(product.productType.eq(type).and(product.productDetailType.eq(detailType)))
				.orderBy(product.productRdate.desc(),product.productName.asc())
				.distinct()
				.fetchResults();
		
		return new PageImpl<>(productList.getResults(), pageable, productList.getTotal());
	}
	
	public Long updateProductQuantity(Product selectedProduct) {
		// TODO: 트러블 슈팅 정리 
		/*
		long result = queryFactory.update(product)
			.where(product.productId.eq(selectedProduct.getProductId()).and(product.productQuantity.gt(0)))
			.set(product.productQuantity, selectedProduct.getProductQuantity()-1)
			.execute();
		
		return result;
		*/
		
		long result = queryFactory.update(product)
				.where(product.productId.eq(selectedProduct.getProductId()).and(product.productQuantity.gt(0)))
				.set(product.productQuantity, selectedProduct.getProductQuantity()-1)
				.execute();
			
		return result;
	}

}
