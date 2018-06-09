package model;

public class Product {
    private String title;
    private String description;
    private Double pricePerUnit;
    private Double pricePer100g;
    private Double kCalPer100g;

    public Product(String title, String description, Double pricePerUnit, Double pricePer100g, Double kCalPer100g) {
        this.title = title;
        this.description = description;
        this.pricePerUnit = pricePerUnit;
        this.pricePer100g = pricePer100g;
        this.kCalPer100g = kCalPer100g;
    }

    public Product(String title, String description, Double pricePerUnit, Double pricePer100g) {
        this(title, description, pricePerUnit, pricePer100g, null);
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", pricePerUnit=" + pricePerUnit +
                ", pricePer100g=" + pricePer100g +
                ", kCalPer100g=" + kCalPer100g +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Double getPricePer100g() {
        return pricePer100g;
    }

    public void setPricePer100g(Double pricePer100g) {
        this.pricePer100g = pricePer100g;
    }

    public Double getkCalPer100g() {
        return kCalPer100g;
    }

    public void setkCalPer100g(Double kCalPer100g) {
        this.kCalPer100g = kCalPer100g;
    }
}
