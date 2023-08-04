package telran.spring;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import telran.spring.exceptions.NotFoundException;
import telran.spring.model.Product;
import telran.spring.service.ProductService;
import telran.spring.service.ProductServiceImpl;

@SpringBootTest

class ProductServiceTest {
	@Autowired
	ProductServiceImpl productService;
	
	@BeforeEach
	void clearProducts() {
	    productService.clear();
	}
	
	
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
	
	@Test
	void testAddProductWithInvalidName() {
	    Product product = new Product();
	    product.setCategory("Electronics");
	    product.setPrice(1000);
	    
	    Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.addProduct(product));
	    
	    assertEquals("Name cannot be null or empty", exception.getMessage());
	}
	
	@Test
	void testDeleteNonExistentProduct() {
	    int nonExistentId = 9999;
	    
	    Exception exception = assertThrows(NotFoundException.class, () -> productService.deleteProduct(nonExistentId));
	    
	    assertEquals("Product with ID " + nonExistentId + " not found", exception.getMessage());
	}
	
	@Test
	void testEditNonExistentProduct() {
	    int nonExistentId = 9999;
	    Product product = createProduct("New Name", "Electronics", 1000);
	    
	    Exception exception = assertThrows(NotFoundException.class, () -> productService.editProduct(nonExistentId, product));
	    
	    assertEquals("Product with ID " + nonExistentId + " not found", exception.getMessage());
	}
	
	@Test
	void testGetProductsByNegativePrice() {
	    int negativePrice = -1000;
	    
	    Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductsByPrice(negativePrice));
	    
	    assertEquals("Max price should be greater than 0", exception.getMessage());
	}
	
	

	
}
