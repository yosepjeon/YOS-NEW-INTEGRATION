//package com.yosep.msa.productapi.service;
//
//import static org.mockito.BDDMockito.given;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.DisplayNameGeneration;
//import org.junit.jupiter.api.DisplayNameGenerator;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort.Direction;
//import org.springframework.data.web.PageableDefault;
//
//import com.yosep.msa.productapi.entity.Product;
//import com.yosep.msa.productapi.entity.ProductDetailType;
//import com.yosep.msa.productapi.entity.ProductType;
//import com.yosep.msa.productapi.repository.ProductProfileFileRepository;
//import com.yosep.msa.productapi.repository.ProductRepository;
//import com.yosep.msa.productapi.repository.ProductRepositorySupport;
//
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
//public class ProductServiceTest {
//
////	@Autowired
////	private ProductService productService;
////	@Autowired
////	private ProductRepository productRepository;
//	
//	@Autowired
//	private ProductService productService;
//	
//	@Autowired
//	private ProductRepository productRepository;
////	@Mock
////	private ProductRepository productRepository;
////	
////	@Mock
////	private ProductRepositorySupport productRepositorySupport;
////	
////	@Mock
////	private ProductProfileFileRepository productProfileFileRepository;
//	
//	Pageable pageable;
//	
////	@BeforeEach
////	public void setUp() {
////		MockitoAnnotations.initMocks(this);
////		mockProductRepository();
//////		productService = new ProductService(productRepository,productRepositorySupport,productProfileFileRepository, new OrderSender());
////	}
//
//	@Test
//	@DisplayName("등록일을 기준으로 내림차순으로 정렬하여 상품 가져오기 테스트")
//	public void getProductsByRDateTest() {
//		Product product = Product.builder().productId("p3").productName("p3").productSale(0).productPrice(30000)
//				.productQuantity(100).productDetail("p3 입니다").productType(ProductType.TOP)
//				.productDetailType(ProductDetailType.SLEEVELESS).build();
//		productRepository.save(product);
//
//		List<Product> products = productService.getProductsOrderByRDate();
//		System.out.println("********************************************");
//		System.out.println("getProductsByRDateTest");
//		System.out.println(products);
//		System.out.println("********************************************");
////		System.out.println(products.get(0).toString());
//	}
//	
//	@Test
//	@DisplayName("상품 가져오기 테스트")
//	public void getProductsTest() {
//		Page<Product> products = productService.getProducts(pageable);
//		System.out.println("********************************************");
//		System.out.println("getProducts");
//		System.out.println(products);
//		System.out.println("********************************************");
//	}
//
//	@Test
//	public void getProductTest() {
//		
//		Optional<Product> product = productService.getProduct("test1");
//		System.out.println("********************************************");
//		System.out.println("getProduct");
//		System.out.println(product.get().toString());
//		System.out.println("********************************************");
//	}
//	
//	@Test
//	@DisplayName("주문 후 주문 엔티티 내의 상품 아이디를 사용하여 주문 관련 상품 목록 가져오기 테스트")
//	public void getProductsByProductIdListTest() {
//		
//	}
//
//	private void mockProductRepository() {
//		List<Product> products = new ArrayList<>();
//		
//		List<Integer> intList = Arrays.asList(1,2,3,4,5);
//		
//		intList.forEach(i -> {
//			Product product = Product.builder().productId("test" + i).productName("test" + i).productSale(0).productPrice(30000)
//					.productQuantity(100).productDetail("test3 입니다").productType(ProductType.TOP)
//					.productDetailType(ProductDetailType.SLEEVELESS).build();
//			
//			products.add(product);	
//		});
//		
////		given(productRepository.findAll(Sort.by(Sort.Direction.DESC,"product_rdate")));
//		given(productRepository.findAll()).willReturn(products);
////		given(productRepository.findById("p3")).willReturn(Optional.of(product));
//	}
//	
//	public Pageable setPageable(@PageableDefault(sort = { "id" }, direction = Direction.DESC, size = 2) Pageable pageable) {
//		return pageable;
//	}
//
//}
