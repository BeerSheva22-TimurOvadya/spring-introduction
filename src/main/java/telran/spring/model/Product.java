package telran.spring.model;

import lombok.Data;

@Data
public class Product {
    private int id;
    private String name;
    private String category;
    private int price;
    private String additionalFields;
}
