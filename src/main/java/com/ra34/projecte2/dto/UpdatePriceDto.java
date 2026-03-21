package com.ra34.projecte2.dto;

// DTO para actualizar el precio de un producto
public class UpdatePriceDto {
    private Double price;

    public UpdatePriceDto() {
    }

    public UpdatePriceDto(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
