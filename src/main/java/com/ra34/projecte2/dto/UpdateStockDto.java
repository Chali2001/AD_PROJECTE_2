package com.ra34.projecte2.dto;

//DTO para actualizar el stock de un producto
public class UpdateStockDto {
    private Integer stock;

    public UpdateStockDto() {
    }

    public UpdateStockDto(Integer stock) {
        this.stock = stock;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
