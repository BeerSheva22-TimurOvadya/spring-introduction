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
	
	 @Bean
	    public ProductService productService() {
	        return new MockProductService();
	    }
	 
	 
	 class MockProductService implements ProductService {

	 	@Override
	 	public Product addProduct(Product product) {
	 		if (product.getName() == null || product.getName().trim().isEmpty()) {
	 	        throw new IllegalArgumentException("Name cannot be null or empty");
	 	    }
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
	 		if (maxPrice < 0) {
	 	        throw new IllegalArgumentException("Max price should be greater than 0");
	 	    }
	 		return Arrays.asList(new Product(1, "test", "test", maxPrice, null));
	 	}

	 	@Override
	 	public Product editProduct(int id, Product product) {
	 		if (id == 9999) {
	 	        throw new NotFoundException("Product with ID " + id + " not found");
	 	    }		
	 		return product;
	 	}

	 	@Override
	 	public void deleteProduct(int id) {
	 		  if (id == 9999) {
	 		        throw new NotFoundException("Product with ID " + id + " not found");
	 		    }
	 	}

	 	@Override
	 	public List<Product> addProducts(List<Product> products) {
	 		return products;
	 	}

	 }

}
