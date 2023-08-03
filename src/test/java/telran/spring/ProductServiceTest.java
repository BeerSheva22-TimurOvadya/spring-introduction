package telran.spring;


import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.spring.model.Product;
import telran.spring.service.ProductService;
import telran.spring.service.ProductServiceImpl;

@SpringBootTest
class ProductServiceTest {
	@Autowired
	ProductService productService;

	
	private List<Product> addSampleProducts() {
		List<Product> products = new ArrayList<>();
		products.add(productService.addProduct(createProduct("Phone", "Electronics", 1000)));
		products.add(productService.addProduct(createProduct("Laptop", "Electronics", 2000)));
		products.add(productService.addProduct(createProduct("Shirt", "Clothing", 50)));
		return products;
	}

	private Product createProduct(String name, String category, int price) {
		Product product = new Product();
		product.setName(name);
		product.setCategory(category);
		product.setPrice(price);
		return product;
	}

	@Test
	void testAddProduct() {
		Product product = new Product();
		product.setCategory("Electronics");
		product.setName("Phone");
		product.setPrice(1000);
		Product addedProduct = productService.addProduct(product);
		
		assertNotNull(addedProduct);
		assertEquals(addedProduct.getName(), product.getName());
		assertEquals(addedProduct.getCategory(), product.getCategory());
		assertEquals(addedProduct.getPrice(), product.getPrice());
		assertNotNull(addedProduct.getId());
	}

	@Test
	void testGetAllProducts() {
		List<Product> products = addSampleProducts();
		List<Product> retrievedProducts = productService.getAllProducts();
		assertEquals(products.size(), retrievedProducts.size());
	}

	@Test
	void testGetProductsByCategory() {
		addSampleProducts();
		List<Product> retrievedProducts = productService.getProductsByCategory("Electronics");
		assertEquals(2, retrievedProducts.size());
	}

	@Test
	void testGetProductsByPrice() {
	    addSampleProducts();
	    List<Product> retrievedProducts = productService.getProductsByPrice(1500);	    
	    assertEquals(2, retrievedProducts.size());
	}

	@Test
	void testEditProduct() {
		List<Product> products = addSampleProducts();
		Product product = products.get(0);
		product.setName("New Name");
		Product editedProduct = productService.editProduct(product.getId(), product);
		assertEquals("New Name", editedProduct.getName());
	}

	@Test
	void testDeleteProduct() {
		List<Product> products = addSampleProducts();
		int productId = products.get(0).getId();
		productService.deleteProduct(productId);
		List<Product> retrievedProducts = productService.getAllProducts();
		assertEquals(2, retrievedProducts.size());
	}

	@Test
	void testAddProducts() {
		Product product1 = createProduct("Phone", "Electronics", 1000);
		Product product2 = createProduct("Laptop", "Electronics", 2000);
		List<Product> products = Arrays.asList(product1, product2);
		List<Product> addedProducts = productService.addProducts(products);
		assertEquals(2, addedProducts.size());
	}

	
}
