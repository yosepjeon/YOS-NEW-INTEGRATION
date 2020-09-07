package com.yosep.msa.yosfrontserver.client;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.yosep.msa.yosfrontserver.entity.Product;
import com.yosep.msa.yosfrontserver.entity.ProductDTO;
import com.yosep.msa.yosfrontserver.entity.ProductDtoForRestTemplate;
import com.yosep.msa.yosfrontserver.entity.ProductProfileFile;
import com.yosep.msa.yosfrontserver.entity.ProductRow;
import com.yosep.msa.yosfrontserver.fileIO.S3Uploader;

@CrossOrigin
@Controller
public class ProductController {
	@SuppressWarnings({ "unused", "unused" })
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Value("file.upload-dir")
	private static String root;

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private S3Uploader s3Uploader;

	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@RequestMapping(value = "/product/{type}/{detailType}", method = RequestMethod.GET)
	public ModelAndView showProductPageTest(@PathVariable("type") String type,
			@PathVariable("detailType") String detailType, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "page-size", defaultValue = "9") int pageSize,
			@RequestParam(value = "row-size", defaultValue = "3") int rowSize) {
		ObjectMapper objectMapper = new ObjectMapper();
		TypeFactory typeFactory = objectMapper.getTypeFactory();
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		
		ModelAndView mav = new ModelAndView();
		type = type.toUpperCase();
		detailType = detailType.toUpperCase();
		
		System.out.println(type + "/" + detailType);
		
		ResponseEntity response = restTemplate
				.getForEntity("http://52.78.74.150:8075/api/products/" + type + "/" + detailType, Map.class);
		
		System.out.println(response);

		LinkedHashMap<String, Object> responseBody = (LinkedHashMap<String, Object>) response.getBody();
		LinkedHashMap<String, Object> embedded = (LinkedHashMap<String, Object>) responseBody.get("_embedded");

		if(embedded == null) {
			mav.setViewName("product");
			return mav;
		}
		
		List<Product> productsRow = new ArrayList<>();
//		List<Product> products = objectMapper.convertValue(embedded.get("productList"), new TypeReference<List<Product>>() {});
		List<EntityModel<Product>> productEntityModelList = objectMapper.convertValue(embedded.get("productList"),
				new TypeReference<List<EntityModel<Product>>>() {
				});
		
//		System.out.println(productEntityModelList);
//		System.out.println(productEntityModelList.get(0).getClass());
//		System.out.println(productEntityModelList.get(0).getContent().getClass());

		List<Product> products = new ArrayList<>();
		List<ProductRow> productRows = new ArrayList<>();
		List<Product> productRow = new ArrayList<>();

		productEntityModelList.forEach(element -> {
			products.add(element.getContent());
		});
		System.out.println("products Size: " + products.size());
		
		if(products.size() > rowSize) {
			System.out.println("초과");
			int count = 1;
			for (Product p : products) {
				productRow.add(p);
				
				if (count % rowSize == 0) {
					productRows.add(new ProductRow(productRow));
					productRow = new ArrayList<>();
					count++;
					continue;
				}
				
				count++;
			}
			
			if(productRow.size() > 0) {
				productRows.add(new ProductRow(productRow));
			}
		}else {
			System.out.println("미달");
			int count = 1;
			for (Product p : products) {
				productRow.add(p);
			}
			
			productRows.add(new ProductRow(productRow));
		}
		
		System.out.println("총 크기 " + productRows.size());

		mav.addObject("itemRows", productRows);
		mav.addObject("products", responseBody);
		mav.addObject("type", type);
		mav.addObject("detailType", detailType);
		mav.setViewName("product");
		return mav;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/detail_product/{type}/{detailType}", method = RequestMethod.GET)
	public ModelAndView showProductDetailPage(@PathVariable("type") String type,
			@PathVariable("detailType") String detailType, @RequestParam(value = "productId") String productId,
			HttpSession session) {
		ModelAndView mav = new ModelAndView();
//		Product product = restTemplate.getForObject(
//				"http://localhost:8075/api/products/" + productId, Product.class);
		ResponseEntity response = restTemplate.getForEntity("http://52.78.74.150:8075/api/products/" + productId, Map.class);
		LinkedHashMap<String, Object> responseBody = (LinkedHashMap<String, Object>) response.getBody();
		
		Product product = Product.builder()
				.productId(String.valueOf(responseBody.get("productId")))
				.productName(String.valueOf(responseBody.get("productName")))
				.productSale(Integer.valueOf(String.valueOf(responseBody.get("productSale"))))
				.productPrice(Integer.valueOf(String.valueOf(responseBody.get("productPrice"))))
				.productQuantity(Integer.valueOf(String.valueOf(responseBody.get("productQuantity"))))
				.productDetail(String.valueOf(responseBody.get("productDetail")))
				.productProfileImageURLs((List<ProductProfileFile>) responseBody.get("productProfileImageURLs"))
				.build();
		System.out.println(product.toString());
		
		mav.addObject("userId", (String) session.getAttribute("userId"));
		mav.addObject("product", product);
		mav.addObject("type", type);
		mav.addObject("detailType", detailType);
		mav.setViewName("detail_product");
		return mav;
	}

	@RequestMapping(value = "/insert_product/{type}/{detailType}", method = RequestMethod.GET)
	public ModelAndView showProductInsertPage(@PathVariable("type") String type,
			@PathVariable("detailType") String detailType) {
		ModelAndView mav = new ModelAndView();

		mav.addObject("type", type);
		mav.addObject("detailType", detailType);
		mav.setViewName("insert_product");

		return mav;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping(value = "/createProduct/{type}/{detailType}", method = RequestMethod.POST)
	public ModelAndView createProduct(@PathVariable("type") String type, @PathVariable("detailType") String detailType,
			ProductDTO productDTO) throws Exception {
		type = type.toUpperCase();
		detailType = detailType.toUpperCase();
		
		ArrayList<MultipartFile> productDescriptionFileArray = new ArrayList<>();
		String storeDescriptionFileURL;
//		if (!productDescription1_file.isEmpty()) {
//			String fileType = productDescription1_file.getOriginalFilename()
//					.substring(productDescription1_file.getOriginalFilename().lastIndexOf("."));
//			storeDescriptionFileURL = root + "/" + type + "/" + detailType + "/description/" + product.getProductId()
//					+ fileType;
//			product.getProductDescriptions().add(new ProductDescription(productDescription1, storeDescriptionFileURL));
//		}
//
//		if (!productDescription2_file.isEmpty()) {
//			String fileType = productDescription2_file.getOriginalFilename()
//					.substring(productDescription2_file.getOriginalFilename().lastIndexOf("."));
//			storeDescriptionFileURL = root + "/" + type + "/" + detailType + "/description/" + product.getProductId()
//					+ fileType;
//			product.getProductDescriptions().add(new ProductDescription(productDescription2, storeDescriptionFileURL));
//		}
//
//		if (!productDescription3_file.isEmpty()) {
//			String fileType = productDescription3_file.getOriginalFilename()
//					.substring(productDescription3_file.getOriginalFilename().lastIndexOf("."));
//			storeDescriptionFileURL = root + "/" + type + "/" + detailType + "/description/" + product.getProductId()
//					+ fileType;
//			product.getProductDescriptions().add(new ProductDescription(productDescription3, storeDescriptionFileURL));
//		}

		List<String> productProfileImageFilesURLs = new ArrayList<>();
		ProductDtoForRestTemplate productDtoForRestTemplate = ProductDtoForRestTemplate.builder()
				.productId(productDTO.getProductId()).productName(productDTO.getProductName())
				.productPrice(productDTO.getProductPrice()).productQuantity(productDTO.getProductQuantity())
				.productSale(productDTO.getProductSale()).productDetail(productDTO.getProductDetail())
				.productProfileImageURLs(productProfileImageFilesURLs).build();

		List<MultipartFile> productProfileImageFiles = productDTO.getProductProfileImageFiles();

		for (MultipartFile m : productProfileImageFiles) {
//			fileComponent.storeProductProfileImageFiles(product, m, type, detailType);
			s3Uploader.upload(productDtoForRestTemplate, m, "yosep-spring-shoppingsite-msa-product" + File.separator
					+ type + File.separator + detailType + File.separator + productDtoForRestTemplate.getProductId());
		}

		ResponseEntity response = restTemplate.postForEntity(
				"http://52.78.74.150:8075/api/products/" + type + "/" + detailType + "/create", productDtoForRestTemplate,
				Map.class);

		ModelAndView mv = new ModelAndView();
		mv.addObject("createResponse", response);
		mv.setViewName("main");

		System.out.println("createProduct result: " + response.getStatusCodeValue());

		if (response.getStatusCodeValue() == 201) {
			return mv;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unused")
	private void toUpperTypeAndDetailType(String type, String detailType) {
		type = type.toUpperCase();
		detailType = detailType.toUpperCase();
	}
}
