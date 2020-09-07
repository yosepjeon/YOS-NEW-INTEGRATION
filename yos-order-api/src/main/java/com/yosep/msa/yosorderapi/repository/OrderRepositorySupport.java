package com.yosep.msa.yosorderapi.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.msa.yosorderapi.entity.Order;
import com.yosep.msa.yosorderapi.entity.OrderState;
import com.yosep.msa.yosorderapi.entity.QOrder;

@Repository
public class OrderRepositorySupport extends QuerydslRepositorySupport {
	@Autowired
	private final JPAQueryFactory queryFactory;
	QOrder order = QOrder.order;
	
	public OrderRepositorySupport(JPAQueryFactory queryFactory) {
		super(Order.class);
		this.queryFactory = queryFactory;
	}

	public List<Order> findOrdersBeforeBuyByUserId(String userId) {

		return queryFactory.selectFrom(order).where(order.senderId.eq(userId).and(order.isBuy.eq(false)))
				.orderBy(order.orderRdate.desc()).distinct().fetch();
	}
	
//	public List<Order> findOrderFailedBuyButSuccessPay() {
//		return queryFactory.selectFrom(order)
//				
//	}

	public long updateOrderProcessSuccess(String orderId) {
		return queryFactory.update(order)
				.where(order.orderId.eq(orderId))
				.set(order.isBuy, true)
				.set(order.state, OrderState.SUCCESS)
				.execute();
	}

	public long updateOrderProcessFail(String orderId) {
		return queryFactory.update(order)
				.where(order.orderId.eq(orderId))
				.set(order.isBuy, false)
				.set(order.state, OrderState.BILLING_SUCCESS_BUT_BUY_FAIL)
				.execute();
	}
	
	public List<Order> findOrdersToSendMMS() {
		return queryFactory.selectFrom(order)
				.where(order.state.eq(OrderState.BILLING_SUCCESS_BUT_BUY_FAIL))
				.fetch();
	}
	
	public long updateOrderWaitRefund(String orderId) {
		return queryFactory.update(order)
				.where(order.orderId.eq(orderId))
				.set(order.state, OrderState.WAIT_REFUND)
				.execute();
	}
}
