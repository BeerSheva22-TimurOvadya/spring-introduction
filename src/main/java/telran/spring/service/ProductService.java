package telran.spring.service;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Product;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class ProductService {
	private Map<Integer, Product> productsById = new HashMap<>();
	private Map<String, List<Product>> productsByCategory = new HashMap<>();
	private TreeMap<Integer, List<Product>> productsByPrice = new TreeMap<>();

	public Product addProduct(Product product) {
		int id = generateUniqueId();
		product.setId(id);
		productsById.put(id, product);
		productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
		productsByPrice.computeIfAbsent(product.getPrice(), k -> new ArrayList<>()).add(product);
		log.info("Adding product: " + product);

		return product;
	}

	public List<Product> getAllProducts() {
		List<Product> result = new ArrayList<>(productsById.values());
		log.info("All products have been retrieved");
		return result;
	}

	public List<Product> getProductsByCategory(String category) {
		List<Product> result = new ArrayList<>(productsByCategory.getOrDefault(category, Collections.emptyList()));
		log.info("Products have been retrieved by category: " + category);
		return result;
	}

	public List<Product> getProductsByPrice(int maxPrice) {
		List<Product> result = new ArrayList<>();
		productsByPrice.headMap(maxPrice).values().forEach(result::addAll);
		log.info("Products have been retrieved by max price: " + maxPrice);
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
        log.info("Edited product: " + product);
        return product;
    }

	public void deleteProduct(int id) {
        removeProductFromMaps(productsById.remove(id));
        log.info("Deleted product by ID: " + id);
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
