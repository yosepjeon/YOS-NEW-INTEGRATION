package com.yosep.msa.yosorderapi.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.yosorderapi.entity.Cart;
import com.yosep.msa.yosorderapi.entity.QCart;

@Repository
public class CartRepositorySupport extends QuerydslRepositorySupport{
	@Autowired
	private final JPAQueryFactory queryFactory;
	QCart cart = QCart.cart;
	
	public CartRepositorySupport(JPAQueryFactory queryFactory) {
		super(Cart.class);
		this.queryFactory = queryFactory;
	}

	public List<Cart> findCartByUserId(String userId) {
		return queryFactory.selectFrom(cart)
				.where(cart.userName.eq(userId))
				.orderBy(cart.orderRdate.desc())
				.fetch();
	}
	
	public long deleteCartElementByOrderId(String orderId) {
		return queryFactory.delete(cart)
			.where(cart.orderId.eq(orderId))
			.execute();
	}
}
