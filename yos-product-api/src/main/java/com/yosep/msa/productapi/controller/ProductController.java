package com.yosep.msa.productapi.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yosep.msa.productapi.common.ErrorsResource;
import com.yosep.msa.productapi.entity.Product;
import com.yosep.msa.productapi.entity.ProductDTO;
import com.yosep.msa.productapi.entity.ProductDetailType;
import com.yosep.msa.productapi.entity.ProductResource;
import com.yosep.msa.productapi.entity.ProductType;
import com.yosep.msa.productapi.entity.ProductValidator;
import com.yosep.msa.productapi.exception.ProductNotFoundException;
import com.yosep.msa.productapi.service.ProductService;

@RestController
@CrossOrigin
@RefreshScope
@RequestMapping(value = "/api/products", produces = MediaTypes.HAL_JSON_VALUE)
public class ProductController {
	@Value("${spring.application.name}")
	public String value1;

	private final ProductService productService;
	private final ModelMapper modelMapper;
	private final WebMvcLinkBuilder controllerLinkBuilder;
	private ProductResource productResource;
	
	@Autowired
	public ProductController(ProductService productService, ModelMapper modelMapper) {
		// TODO Auto-generated constructor stub
		this.productService = productService;
		this.modelMapper = modelMapper;
		controllerLinkBuilder = linkTo(ProductController.class);
	}
	
	@GetMapping("/config-value")
	public String testConfigValue() {
		return value1;
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@PostMapping("/{type}/{detailType}/create")
	public ResponseEntity createProduct(@PathVariable("type") String type,@PathVariable("detailType")String detailType,@RequestBody @Valid ProductDTO productDTO, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		ProductValidator.createValidate(productDTO, errors);
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		Product product = modelMapper.map(productDTO, Product.class);
		product.setProductType(ProductType.valueOf(type));
		product.setProductDetailType(ProductDetailType.valueOf(detailType));
		Product newProduct = this.productService.create(product,productDTO);
		
		productResource = new ProductResource(newProduct);
		productResource.add(controllerLinkBuilder.withRel("get-products"));
		productResource.add(controllerLinkBuilder.slash(product.getProductId()).withRel("get-product"));
		productResource.add(controllerLinkBuilder.slash(product.getProductId()).withRel("patch-product"));
		productResource.add(new Link("/docs/index.html#create-product").withRel("profile"));
		
		URI createdURI = controllerLinkBuilder.slash(newProduct.getProductId()).toUri();
		return ResponseEntity.created(createdURI).body(productResource);
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/{type}/{detailType}/createtest")
	public ResponseEntity createProductTest(@PathVariable("type") String type,@PathVariable("detailType")String detailType,@RequestBody @Valid ProductDTO productDTO, Errors errors) {
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		
		return ResponseEntity.ok().build();
	}

	@SuppressWarnings({ "rawtypes", "deprecation" })
	@GetMapping("/{id}")
	public ResponseEntity getProduct(@PathVariable String id) {
		@SuppressWarnings("unused")
		URI productURI = controllerLinkBuilder.slash(id).toUri();
//		ProductResource productResource;
		
		try {
			Optional<Product> optionalProduct = productService.getProduct(id);
			if(!optionalProduct.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			
			Product product = optionalProduct.get();
			
			productResource = new ProductResource(product);
			productResource.add(controllerLinkBuilder.withRel("get-products"));
			productResource.add(controllerLinkBuilder.slash(id).withRel("get-product"));
			productResource.add(controllerLinkBuilder.slash(id).withRel("patch-product"));
			productResource.add(new Link("/docs/index.html#product-get").withRel("profile"));
			
			return ResponseEntity.ok().body(productResource);
		} catch (ProductNotFoundException pne) {
			// TODO: handle exception
			return ResponseEntity.notFound().build();
		}
//		URI elementUri = controllerLinkBuilder.slash("");
//		
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@PatchMapping("/{id}")
	public ResponseEntity patchProduct(@PathVariable String id, @RequestBody @Valid ProductDTO productDTO, Errors errors) {
		Optional<Product> optionalProduct = this.productService.getProduct(id);
		
		if(!optionalProduct.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		// TODO: productValidator 구현
		
		Product targetProduct = optionalProduct.get();
		
		this.modelMapper.map(productDTO, targetProduct);
		Product updatedProduct = productService.patchProduct(targetProduct);
		
		ProductResource productResource = new ProductResource(updatedProduct);
		productResource.add(controllerLinkBuilder.withRel("get-products"));
		productResource.add(controllerLinkBuilder.slash(id).withRel("get-product"));
		productResource.add(controllerLinkBuilder.slash(id).withRel("patch-product"));
		productResource.add(new Link("/docs/index.html#patch-product").withRel("profile"));
		
		return ResponseEntity.ok(productResource);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/cart")
	public ResponseEntity getProductsByCart(@RequestParam("productIdList") List<String> productIdList) {
		List<Product> productList = productService.getProductsByProductIdList(productIdList);
		
		return ResponseEntity.ok(productList);
	}
	
	@SuppressWarnings({ "deprecation", "rawtypes" })
	@GetMapping("")
	public ResponseEntity getProducts(Pageable pageable, PagedResourcesAssembler<Product> assembler) {
		Page<Product> page = this.productService.getProducts(pageable);
		PagedModel<EntityModel<Product>> pagedResources = assembler.toModel(page, p -> {
			ProductResource productResource = new ProductResource(p);
			productResource.add(controllerLinkBuilder.withRel("get-products"));
			productResource.add(controllerLinkBuilder.slash(p.getProductId()).withRel("get-product"));
			productResource.add(controllerLinkBuilder.slash(p.getProductId()).withRel("patch-product"));
			return productResource;
			
		});
		pagedResources.add(new Link("/docs/index.html#get-products").withRel("profile"));
		
		return ResponseEntity.ok(pagedResources);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/target")
	public ResponseEntity getProductsByProductIdList(@RequestParam("productIdList") List<String> productIdList) {
		System.out.println(productIdList.size());
		
		return ResponseEntity.ok().build();
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@GetMapping("/{type}/{detailType}")
	public ResponseEntity getProductsFilteredByTypeAndDetailType(@PageableDefault(size=9) Pageable pageable, PagedResourcesAssembler<Product> assembler,@PathVariable("type")String productType, @PathVariable("detailType")String productDetailType) {
		if(pageable.getPageSize() == 20) {
			
		}
		
		Page<Product> page = this.productService.getProductsByTypeAndDetailType(pageable, productType, productDetailType);
		PagedModel<EntityModel<Product>> pagedResources = assembler.toModel(page, p -> {
			ProductResource productResource = new ProductResource(p);
			productResource.add(controllerLinkBuilder.withRel("get-products"));
			productResource.add(controllerLinkBuilder.slash(p.getProductId()).withRel("get-product"));
			productResource.add(controllerLinkBuilder.slash(p.getProductId()).withRel("patch-product"));
			return productResource;
			
		});
		pagedResources.add(new Link("/docs/index.html#get-filtered-products").withRel("profile"));
		
		return ResponseEntity.ok(pagedResources);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/dsl")
	public ResponseEntity getProductsDsl() {
		List<Product> products = productService.getProductsByQueryDsl();
		
		return ResponseEntity.ok(products);
	}
	
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/test")
	public ResponseEntity restDocsTestMethod() {
//		Link testMethodLink1 = linkTo(methodOn(ProductController.class).restDocsTestMethod()).withSelfRel();
		
		Link testMethodLink1 = Affordances.of(linkTo(methodOn(ProductController.class).restDocsTestMethod()).withSelfRel())
				.afford(HttpMethod.GET).withInputAndOutput(Product.class).withName("restDocsTestMethod").toLink();
				
		ProductResource productResource = new ProductResource(productService.getProduct("p2").get());
		productResource.add(testMethodLink1);
		return ResponseEntity.ok().body(productResource);
	}
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private ResponseEntity notFound(Errors errors) {
		return ResponseEntity.notFound().build();
	}
}
