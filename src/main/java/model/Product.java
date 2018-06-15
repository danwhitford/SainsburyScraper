package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to hold information on each product. Uses Jackson annotations for JSON format
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("price_per_unit")
    private Double pricePerUnit;
    @JsonProperty("kcal_per_100G")
    private Double kCalPer100G;

    public Product(String title, String description, Double pricePerUnit, Double kCalPer100G) {
        this.title = title;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
        this.kCalPer100G = kCalPer100G;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getKCalPer100G() {
        return kCalPer100G;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }
}
