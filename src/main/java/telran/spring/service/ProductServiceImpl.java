package telran.spring.service;

import org.springframework.stereotype.Service;

import telran.spring.exceptions.NotFoundException;
import telran.spring.model.Product;
import telran.spring.service.ProductService;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductServiceImpl implements ProductService {
	private Map<Integer, Product> productsById = new HashMap<>();
	private Map<String, List<Product>> productsByCategory = new HashMap<>();
	private TreeMap<Integer, List<Product>> productsByPrice = new TreeMap<>();

	public Product addProduct(Product product) {
		if (product.getName() == null || product.getName().trim().isEmpty()) {
	        throw new IllegalArgumentException("Name cannot be null or empty");
	    }
		int id = generateUniqueId();
		product.setId(id);
		productsById.put(id, product);
		productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
		productsByPrice.computeIfAbsent(product.getPrice(), k -> new ArrayList<>()).add(product);
		
		return product;
	}

	public List<Product> getAllProducts() {
		List<Product> result = new ArrayList<>(productsById.values());
		
		return result;
	}

	public List<Product> getProductsByCategory(String category) {
		List<Product> result = new ArrayList<>(productsByCategory.getOrDefault(category, Collections.emptyList()));
		
		return result;
	}

	public List<Product> getProductsByPrice(int maxPrice) {
	    if (maxPrice <= 0) {
	        throw new IllegalArgumentException("Max price should be greater than 0");
	    }
	    List<Product> result = new ArrayList<>();
	    productsByPrice.headMap(maxPrice).values().forEach(result::addAll);
	    return result;
	}

	private Integer generateUniqueId() {
        Integer id;
        do {
            id = ThreadLocalRandom.current().nextInt(100000, 1000000);
        } while (productsById.containsKey(id));
        return id;
    }
	

	public Product editProduct(int id, Product product) {
        removeProductFromMaps(productsById.get(id));
        product.setId(id);
        addProductToMaps(product);
        productsById.put(id, product);
       
        return product;
    }

	public void deleteProduct(int id) {
	    Product product = productsById.remove(id);
	    if (product == null) {
	        throw new NotFoundException("Product with ID " + id + " not found");
	    }
	    removeProductFromMaps(product);
	}

	private void addProductToMaps(Product product) {
		productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
		productsByPrice.computeIfAbsent(product.getPrice(), k -> new ArrayList<>()).add(product);
	}

	private void removeProductFromMaps(Product product) {
		productsByCategory.get(product.getCategory()).remove(product);
		productsByPrice.get(product.getPrice()).remove(product);		
	}

	public List<Product> addProducts(List<Product> products) {
		List<Product> addedProducts = new ArrayList<>();
		for (Product product : products) {
			addedProducts.add(addProduct(product));
		}
		return addedProducts;
	}
}