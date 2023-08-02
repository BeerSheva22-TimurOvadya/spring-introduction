package telran.spring.controller;

import org.springframework.web.bind.annotation.*;

import telran.spring.model.Product;
import telran.spring.service.ProductService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }
    
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/price")
    public List<Product> getProductsByPrice(@RequestParam double maxPrice) {
        return productService.getProductsByPrice(maxPrice);
    }

    @PutMapping("/{id}")
    public Product editProduct(@PathVariable String id, @RequestBody Product product) {
        return productService.editProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
    }
    
    @PostMapping("/batch")
    public List<Product> addProducts(@RequestBody List<Product> products) {
        return productService.addProducts(products);
    }

}
