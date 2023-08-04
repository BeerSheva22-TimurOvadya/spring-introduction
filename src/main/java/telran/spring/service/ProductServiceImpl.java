package telran.spring.service;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
	private Map<Integer, Product> productsById = new HashMap<>();
	private Map<String, List<Product>> productsByCategory = new HashMap<>();
	private TreeMap<Integer, List<Product>> productsByPrice = new TreeMap<>();

	private static final String FILE_NAME = "products.json";

	public Product addProduct(Product product) {
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
		Product editedProduct = productsById.get(id);
		removeProductFromMaps(editedProduct);
		product.setId(id);
		addProductToMaps(product);
		productsById.put(id, product);

		return product;
	}

	public void deleteProduct(int id) {
		Product product = productsById.remove(id);
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

	public void clear() {
		productsById.clear();
		productsByCategory.clear();
		productsByPrice.clear();
	}

	@PostConstruct
	public void init() {
		File file = new File(FILE_NAME);
		if (file.exists()) {
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				List<Product> products = objectMapper.readValue(file,
						objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));
				addProducts(products);
				log.info("Products have been loaded successfully");
			} catch (IOException e) {
				log.error("Failed to load products: ", e);
			}
		}
	}

	@PreDestroy
	public void shutdown() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			objectMapper.writeValue(new File(FILE_NAME), getAllProducts());
			log.info("Products have been saved successfully");
		} catch (IOException e) {
			log.error("Failed to save products: ", e);
		}
	}

}