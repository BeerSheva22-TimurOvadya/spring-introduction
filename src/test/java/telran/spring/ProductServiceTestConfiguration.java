package telran.spring;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import telran.spring.exceptions.NotFoundException;
import telran.spring.model.Product;
import telran.spring.service.ProductService;

@TestConfiguration
public class ProductServiceTestConfiguration {
	public static int TEST_ID = 888888;
	
	 @Bean
	    public ProductService productService() {
	        return new MockProductService();
	    }
	 
	 
	 class MockProductService implements ProductService {

	 	@Override
	 	public Product addProduct(Product product) {
	 		
	 		return product;
	 	}

	 	@Override
	 	public List<Product> getAllProducts() {
	 		return Arrays.asList(new Product(TEST_ID, "test", "test", 100, null));
	 	}

	 	@Override
	 	public List<Product> getProductsByCategory(String category) {
	 		return Arrays.asList(new Product(TEST_ID, "test", category, 100, null));
	 	}

	 	@Override
	 	public List<Product> getProductsByPrice(double maxPrice) {
	 		
	 		return Arrays.asList(new Product(TEST_ID, "test", "test", maxPrice, null));
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

}
