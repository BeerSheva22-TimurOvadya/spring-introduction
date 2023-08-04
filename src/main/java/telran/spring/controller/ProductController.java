package telran.spring.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.constraints.*;
import jakarta.annotation.PreDestroy;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.spring.model.Product;
import telran.spring.service.ProductService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
@Slf4j
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody @Valid Product product) {        
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
    public List<Product> getProductsByPrice(@RequestParam @Min(0) double maxPrice) {
        log.info("Products have been retrieved by max price: " + maxPrice);
        return productService.getProductsByPrice(maxPrice);
    }

    @PutMapping("/{id}")
    public Product editProduct(@PathVariable @Min(100000) @Max(999999) int id, @RequestBody @Valid Product product) {
        log.info("Edited product: " + product);
        return productService.editProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable @Min(100000) @Max(999999) int id) {
        log.info("Deleted product by ID: " + id);
        productService.deleteProduct(id);
    }

    @PostMapping("/batch")
    public List<Product> addProducts(@RequestBody @Valid List<Product> products) {
        return productService.addProducts(products);
    }
    
    @PreDestroy
	void shutdown() {		
		log.info("The server has been stopped");
	}
}
