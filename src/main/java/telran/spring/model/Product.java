package telran.spring.model;

import java.util.HashMap;
import java.util.Map;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;

@Data
public class Product {
    private String id;
    private String name;
    private String category;
    private double price;
    private String propert11ies;

    public Map<String, String> getParsedProperties() {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> propertiesMap = new HashMap<>();
        
        try {
            propertiesMap = objectMapper.readValue(propert11ies, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propertiesMap;
    }
}