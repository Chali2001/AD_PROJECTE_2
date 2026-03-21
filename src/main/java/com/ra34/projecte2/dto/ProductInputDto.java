package com.ra34.projecte2.dto;

import com.ra34.projecte2.model.ProductCondition;

/**
 * DTO para recibir datos de entrada en operaciones POST y PUT de productos.
 * No incluye campos de auditoría ni ID (autogenerado).
 */
public class ProductInputDto {
    private String name;
    private String description;
    private Integer stock;
    private Double price;
    private Double rating;
    private ProductCondition condition;

    public ProductInputDto() {
    }

    public ProductInputDto(String name, String description, Integer stock, Double price, Double rating,
            ProductCondition condition) {
        this.name = name;
        this.description = description;
        this.stock = stock;
        this.price = price;
        this.rating = rating;
        this.condition = condition;
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

    public ProductCondition getCondition() {
        return condition;
    }

    public void setCondition(ProductCondition condition) {
        this.condition = condition;
    }
}
