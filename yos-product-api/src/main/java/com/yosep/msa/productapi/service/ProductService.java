package com.yosep.msa.productapi.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yosep.msa.productapi.entity.OrderMessageQueueAfterBillingDTO;
import com.yosep.msa.productapi.entity.Product;
import com.yosep.msa.productapi.entity.ProductDTO;
import com.yosep.msa.productapi.entity.ProductMessageQueueAboutAfterBiilingProcessDTO;
import com.yosep.msa.productapi.entity.ProductProfileFile;
import com.yosep.msa.productapi.mq.OrderSender;
import com.yosep.msa.productapi.repository.ProductProfileFileRepository;
import com.yosep.msa.productapi.repository.ProductRepository;
import com.yosep.msa.productapi.repository.ProductRepositorySupport;

@Service
//@Transactional
public class ProductService {
	@Autowired
	EntityManager em;

	private final ProductRepository productRepository;
	@SuppressWarnings("unused")
	private final ProductProfileFileRepository productProfileFileRepository;
	private final ProductRepositorySupport productRepositorySupport;
	private final OrderSender orderSender;

	@Autowired
	public ProductService(ProductRepository productRepository, ProductRepositorySupport productRepositorySupport,
			ProductProfileFileRepository productProfileFileRepository, OrderSender orderSender) {
		this.productRepository = productRepository;
		this.productRepositorySupport = productRepositorySupport;
		this.productProfileFileRepository = productProfileFileRepository;
		this.orderSender = orderSender;
	}

	/*
	 * Business Logic 1. 상품을 저장한다. 2. 해당 상품에 대한 프로필 이미지 정보를 저장한다. 3. 저장된 상품 결과물에 저장된
	 * 프로필 이미지 결과물을 더한다. 4. 반환
	 */
	public Product create(Product product, ProductDTO productDTO) {
		product.setProductRdate(LocalDateTime.now());
		product.setProductUdate(LocalDateTime.now());

		String fileId = product.getProductId();
		int index = 0;
		List<ProductProfileFile> ppfList = new ArrayList<>();
		for (String url : productDTO.getProductProfileImageURLs()) {
			ProductProfileFile ppf = ProductProfileFile.builder().fileId(fileId + index).url(url).product(product)
					.rdate(LocalDateTime.now()).udate(LocalDateTime.now()).build();

			ppfList.add(ppf);
			index++;
		}

		product.setProductProfileImageURLs(ppfList);
		Product createdProduct = productRepository.save(product);

		return createdProduct;
	}

	@Transactional(readOnly = true)
	public Optional<Product> getProduct(String id) {
		return productRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Page<Product> getProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}

	// QueryDSL 적용하기 전 사용했던 상품 가져오기 메서드 현재는 Deprecated 비교를 위해 남겨놓았음
	@Transactional(readOnly = true)
	public List<Product> getProductsOrderByRDate() {
		// TODO Auto-generated method stub
		Sort sort = Sort.by(Sort.Direction.DESC, "product_rdate");

		return productRepository.findAll(sort);
	}

	// QueryDSL Test용 메서드
	@Transactional(readOnly = true)
	public List<Product> getProductsByQueryDsl() {

		return productRepositorySupport.findAll();
	}

	@Transactional(readOnly = true)
	public List<Product> getProductsByProductIdList(List<String> productIdList) {
		List<Product> products = new ArrayList<>();

		productIdList.forEach(id -> {
			Optional<Product> product = productRepository.findById(id);

			if (product.isPresent()) {
				products.add(product.get());
			}
		});

		return products;
	}

	/*
	 * Business Logic 1.
	 */
	@Transactional(readOnly = true)
	public Page<Product> getProductsByTypeAndDetailType(Pageable pageable, String productType,
			String productDetailType) {

		return productRepositorySupport.findAllFilteringByTypeAndDetailType(pageable, productType, productDetailType);
	}

	/*
	 * Business Logic 1. 해당 상품에 대해 일부 수정 진행
	 */
	public Product patchProduct(Product targetProduct) {
		// TODO Auto-generated method stub

		return productRepository.save(targetProduct);
	}

	/*
	 * Logic 1. 해당 상품에 대한 수량을 체크 1-1. 수량이 주문하려는 상품의 개수보다 적게 남아 있으면
	 * NotEnoughProductException 발생 후 종료... 1-2. 수량이 주문하려는 상품의 개수보다 많이 남아 있으면 2번으로
	 * 진행 2. 해당 개수만큼 상품 개수 감소 진행
	 */

	@Transactional(readOnly = false)
	public void updateProductQuantityAfterBuy(
			List<ProductMessageQueueAboutAfterBiilingProcessDTO> ProductMessageQueueAboutAfterBiilingProcessDTOs) {
		// TODO: 시간 측정
		List<OrderMessageQueueAfterBillingDTO> orderMessageQueueAfterBiilingDTOs = new ArrayList<>();
		System.out.println("ProductMessageQueueAboutAfterBiilingProcessDTOs Size: "
				+ ProductMessageQueueAboutAfterBiilingProcessDTOs.size());

		ProductMessageQueueAboutAfterBiilingProcessDTOs.forEach(productMessageQueueAboutAfterBiilingProcessDTO -> {
			Product product = null;
			em.clear();
			product = productRepository.findById(productMessageQueueAboutAfterBiilingProcessDTO.getProductId()).get();
			if (product.getProductQuantity() > 0) {
				System.out.println(product.getProductQuantity());

				long resultUpdate = productRepositorySupport.updateProductQuantity(product);

//				if (resultUpdate > 0) {
//					orderMessageQueueAfterBiilingDTOs.add(new OrderMessageQueueAfterBillingDTO(
//							productMessageQueueAboutAfterBiilingProcessDTO.getOrderId(), product.getProductName(),
//							true));
//				} else {
//					orderMessageQueueAfterBiilingDTOs.add(new OrderMessageQueueAfterBillingDTO(
//							productMessageQueueAboutAfterBiilingProcessDTO.getOrderId(), product.getProductName(),
//							false));
//				}
				
				orderMessageQueueAfterBiilingDTOs.add(new OrderMessageQueueAfterBillingDTO(
						productMessageQueueAboutAfterBiilingProcessDTO.getOrderId(), product.getProductName(),
						true));
			}else {
				orderMessageQueueAfterBiilingDTOs.add(new OrderMessageQueueAfterBillingDTO(
						productMessageQueueAboutAfterBiilingProcessDTO.getOrderId(), product.getProductName(),
						false));
			}
		});

		System.out.println("orderMessageQueueAfterBiilingDTOs size: " + orderMessageQueueAfterBiilingDTOs.size());

		orderSender.sendToOrderSuccessBillingProcessChannel(orderMessageQueueAfterBiilingDTOs);
	}

	/*
	 * Logic 1. 해당 상품에 대한 수량을 체크 1-1. 수량이 주문하려는 상품의 개수보다 적게 남아 있으면
	 * NotEnoughProductException 발생 후 종료... 1-2. 수량이 주문하려는 상품의 개수보다 많이 남아 있으면 2번으로
	 * 진행 2. 해당 개수만큼 상품 개수 감소 진행
	 */
	public void updateProductQuantityAfterBuy_optimized(List<String> productList) {
		// TODO: 시간 측정
	}

}
