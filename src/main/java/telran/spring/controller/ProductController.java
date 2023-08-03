package telran.spring.controller;

import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Product;
import telran.spring.service.ProductService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
    	log.info("Adding product: " + product);
        return productService.addProduct(product);
    }
    
    @GetMapping
    public List<Product> getAllProducts() {
    	log.info("All products have been retrieved");
        return productService.getAllProducts();
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
    	log.info("Products have been retrieved by category: " + category);
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/price")
    public List<Product> getProductsByPrice(@RequestParam int maxPrice) {
    	log.info("Products have been retrieved by max price: " + maxPrice);
        return productService.getProductsByPrice(maxPrice);
    }

    @PutMapping("/{id}")
    public Product editProduct(@PathVariable int id, @RequestBody Product product) {
    	 log.info("Edited product: " + product);
        return productService.editProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable int id) {
    	log.info("Deleted product by ID: " + id);
        productService.deleteProduct(id);
    }
    
    @PostMapping("/batch")
    public List<Product> addProducts(@RequestBody List<Product> products) {
        return productService.addProducts(products);
    }

}