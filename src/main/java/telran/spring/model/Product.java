package telran.spring.model;

import lombok.Data;
import java.util.Map;

@Data
public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private Map<String, String> properties;
}