package com.yosep.msa.yosorderapi.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.yosorderapi.entity.Order;
import com.yosep.msa.yosorderapi.entity.QOrder;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderRepositorySupportTest {

	@Autowired
	OrderRepositorySupport orderRepositorySupport;
	
	@Autowired
	EntityManager em;
	
	@Autowired
	JPAQueryFactory query;

	@Test
	@DisplayName("querydsl을 이용하여 장바구니에서 아직 구매하지 않은 주문 가져오기 테스트")
	public void findOrdersBeforeBuyByUserIdTest() {
		QOrder order = QOrder.order;
		String userId = "enekelx1";
		
		List<Order> orders  = query.selectFrom(order)
				.where(order.senderId.eq(userId))
				.orderBy(order.orderRdate.desc())
				.distinct()
				.fetch();
		
		orders.forEach(o -> System.out.println(o));
	}
}
