package telran.spring.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private String properties;

    @JsonIgnore
    public Map<String, String> getParsedProperties() {
        // Здесь можно преобразовать строку JSON в карту или объект в зависимости от вашего JSON-формата
        return new HashMap<>(); // Пример
    }
}
