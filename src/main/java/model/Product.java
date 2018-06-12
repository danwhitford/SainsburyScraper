package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price_per_unit")
    private Double pricePerUnit;

    @JsonProperty("kcal_per_100G")
    private Double kcalPer100G;

    public Product(String title, String description, Double pricePerUnit, Double kcalPer100G) {
        this.title = title;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
        this.kcalPer100G = kcalPer100G;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }
}
