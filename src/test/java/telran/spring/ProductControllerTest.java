package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.ProductController;
import telran.spring.model.Product;
import telran.spring.service.ProductService;

import java.util.Arrays;
import java.util.List;

@Service
class MockProductService implements ProductService {

	@Override
	public Product addProduct(Product product) {
		return product;
	}

	@Override
	public List<Product> getAllProducts() {
		return Arrays.asList(new Product(1, "test", "test", 100, null));
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return Arrays.asList(new Product(1, "test", category, 100, null));
	}

	@Override
	public List<Product> getProductsByPrice(int maxPrice) {
		return Arrays.asList(new Product(1, "test", "test", maxPrice, null));
	}

	@Override
	public Product editProduct(int id, Product product) {
		return product;
	}

	@Override
	public void deleteProduct(int id) {
	}

	@Override
	public List<Product> addProducts(List<Product> products) {
		return products;
	}

}

@WebMvcTest({ ProductController.class, MockProductService.class })
class ProductControllerTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	Product product;
	String productsUrl = "http://localhost:8080/products";
	String categoryUrl = String.format("%s/category/test", productsUrl);
	String priceUrl = String.format("%s/price?maxPrice=200", productsUrl);

	@BeforeEach
	void setUp() {
		product = new Product(1, "test", "test", 100, null);
	}

	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc);
	}

	@Test
	void addProductTest() throws Exception {
		String productJson = mapper.writeValueAsString(product);
		mockMvc.perform(post(productsUrl).contentType(MediaType.APPLICATION_JSON).content(productJson)).andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void getAllProductsTest() throws Exception {
		mockMvc.perform(get(productsUrl)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getProductsByCategoryTest() throws Exception {
		mockMvc.perform(get(categoryUrl)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void getProductsByPriceTest() throws Exception {
		mockMvc.perform(get(priceUrl)).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void editProductTest() throws Exception {
		String productJson = mapper.writeValueAsString(product);
		mockMvc.perform(put(productsUrl + "/1").contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	void deleteProductTest() throws Exception {
		mockMvc.perform(delete(productsUrl + "/1")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	void addProductsTest() throws Exception {
		List<Product> products = Arrays.asList(product, product);
		String productsJson = mapper.writeValueAsString(products);
		mockMvc.perform(post(productsUrl + "/batch").contentType(MediaType.APPLICATION_JSON).content(productsJson))
				.andDo(print()).andExpect(status().isOk());
	}

	@Test
	void addProductWithMissingFieldsTest() throws Exception {
		product.setName(null);
		String productJson = mapper.writeValueAsString(product);
		mockMvc.perform(post(productsUrl).contentType(MediaType.APPLICATION_JSON).content(productJson)).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	void getProductsByInvalidPriceTest() throws Exception {
		String invalidPriceUrl = String.format("%s/price?maxPrice=-200", productsUrl);
		mockMvc.perform(get(invalidPriceUrl)).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	void editNonExistingProductTest() throws Exception {
		String productJson = mapper.writeValueAsString(product);
		mockMvc.perform(put(productsUrl + "/9999").contentType(MediaType.APPLICATION_JSON).content(productJson))
				.andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	void deleteNonExistingProductTest() throws Exception {
		mockMvc.perform(delete(productsUrl + "/9999")).andDo(print()).andExpect(status().isNotFound());
	}

}