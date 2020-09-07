package com.yosep.msa.yosorderapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.yosep.msa.yosorderapi.entity.Cart;
import com.yosep.msa.yosorderapi.repository.CartRepository;
import com.yosep.msa.yosorderapi.repository.CartRepositorySupport;

@RefreshScope
@Service
public class CartService {
	private final CartRepository cartRepository;
	private final CartRepositorySupport cartRepositorySupport;
	
	@Autowired
	public CartService(CartRepository cartRepository,CartRepositorySupport cartRepositorySupport) {
		this.cartRepository = cartRepository;
		this.cartRepositorySupport = cartRepositorySupport;
	}
	
	public void deleteElementCartById(String orderId) {
		cartRepository.deleteById(orderId);
	}
	
	public Cart addElementToCart(Cart cartElement) {
		String userId = cartElement.getUserName();
		String productId = cartElement.getProductId();
		
		cartElement.setOrderRdate(LocalDateTime.now());
		cartElement.setOrderUdate(LocalDateTime.now());
		
		Optional<Cart> duplicateElement = cartRepository.findById(cartElement.getOrderId());
		
		while(duplicateElement.isPresent()) {
			UUID uuid = UUID.randomUUID();
			cartElement.setOrderId(userId + "-" + productId + "-" + uuid);
			duplicateElement = cartRepository.findById(cartElement.getOrderId());
		}
		
		return cartRepository.save(cartElement);
	}
	
	public List<Cart> getCartElementsByUserId(String userId) {
		List<Cart> elements = cartRepositorySupport.findCartByUserId(userId);
		
		return elements;
	}
	
	public void deleteElementById(String orderId) {
		cartRepository.deleteById(orderId);
	}
}
