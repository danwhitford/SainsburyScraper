package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

public class Results {
    private List<Product> products;
    private Total total;

    public Results(List<Product> products) {
        this.products = products;

        double gross = products.stream().map(Product::getPricePerUnit).mapToDouble(Double::doubleValue).sum();
        double vat = gross * 0.2;

        this.total = new Total(gross, vat);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> results = new HashMap<>();
        results.put("results", products);
        results.put("total", total);
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(results);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "JSON parse error";
        }
    }
}