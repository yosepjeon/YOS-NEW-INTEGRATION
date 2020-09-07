package com.yosep.msa.yosorderapi.service;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.yosep.msa.yosorderapi.entity.Cart;
import com.yosep.msa.yosorderapi.repository.CartRepository;
import com.yosep.msa.yosorderapi.repository.CartRepositorySupport;

@RunWith(SpringRunner.class)
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CartServiceTest {
	@Autowired
	CartRepositorySupport cartRepositorySupport;
	
	@Autowired
	CartRepository cartRepository;
	
	@Test
	@DisplayName("유저아이디를 기준으로 장바구니에 담긴 상품을 가져오는 서비스 테스트")
	public void getCartElementsByUserId() {
		String userId = "enekelx1";
		List<Cart> elements = cartRepositorySupport.findCartByUserId(userId);
		
		System.out.println(elements.size());
	}
}
