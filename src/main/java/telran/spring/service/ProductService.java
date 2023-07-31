package telran.spring.service;

import org.springframework.stereotype.Service;

import telran.spring.model.Product;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductService {
    private Map<String, Product> productsById = new HashMap<>();
    private Map<String, List<Product>> productsByCategory = new HashMap<>();
    private TreeMap<Double, List<Product>> productsByPrice = new TreeMap<>();

    public Product addProduct(Product product) {
        String id = generateUniqueId();
        product.setId(id);

        productsById.put(id, product);
        productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
        productsByPrice.computeIfAbsent(product.getPrice(), k -> new ArrayList<>()).add(product);

        return product;
    }

    public List<Product> getProductsByCategory(String category) {
        return productsByCategory.getOrDefault(category, Collections.emptyList());
    }

    public List<Product> getProductsByPrice(double maxPrice) {
        List<Product> result = new ArrayList<>();
        productsByPrice.headMap(maxPrice).values().forEach(result::addAll);
        return result;
    }

    private String generateUniqueId() {
        String id;
        do {
            id = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        } while (productsById.containsKey(id));
        return id;
    }

    public Product editProduct(String id, Product product) {
        removeProductFromMaps(productsById.get(id));
        product.setId(id);
        addProductToMaps(product);
        productsById.put(id, product);
        return product;
    }

    public void deleteProduct(String id) {
        removeProductFromMaps(productsById.remove(id));
    }

    private void addProductToMaps(Product product) {
        productsByCategory.computeIfAbsent(product.getCategory(), k -> new ArrayList<>()).add(product);
        productsByPrice.computeIfAbsent(product.getPrice(), k -> new ArrayList<>()).add(product);
    }

    private void removeProductFromMaps(Product product) {
        productsByCategory.get(product.getCategory()).remove(product);
        productsByPrice.get(product.getPrice()).remove(product);
    }
}
