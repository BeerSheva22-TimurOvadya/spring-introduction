package telran.spring.service;

import telran.spring.model.Product;
import java.util.List;

public interface ProductService {
    Product addProduct(Product product);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByPrice(double maxPrice);
    Product editProduct(int id, Product product);
    void deleteProduct(int id);
    List<Product> addProducts(List<Product> products);    
}