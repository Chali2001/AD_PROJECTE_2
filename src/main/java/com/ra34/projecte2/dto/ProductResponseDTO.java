package com.ra34.projecte2.dto;

import com.ra34.projecte2.model.ProductCondition;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private Double price;
    private Double rating;
    private ProductCondition conditionStatus;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getStock() {
        return stock;
    }
    public void setStock(Integer stock) {
        this.stock = stock;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(Double rating) {
        this.rating = rating;
    }
    public ProductCondition getConditionStatus() {
        return conditionStatus;
    }
    public void setConditionStatus(ProductCondition conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

  
}