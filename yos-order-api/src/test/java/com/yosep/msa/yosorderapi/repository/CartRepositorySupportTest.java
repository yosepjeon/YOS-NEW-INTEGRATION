package com.yosep.msa.yosorderapi.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.yosorderapi.entity.Cart;
import com.yosep.msa.yosorderapi.entity.QCart;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CartRepositorySupportTest {
	@Autowired
	CartRepositorySupport cartRepositorySupport;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	JPAQueryFactory query;
	
	@Test
	@DisplayName("유저 아이디를 기준으로 카트에 담긴 상품 가져오기")
	public void findCartByUserId() {
		QCart cart = QCart.cart;
		String userId = "test";
		
		List<Cart> elements = query.selectFrom(cart)
				.where(cart.userName.eq(userId))
				.orderBy(cart.orderRdate.desc())
				.fetch();
		
		elements.forEach(e -> {
			System.out.println(e.toString());
		});
	}
}
