package com.ra34.projecte2.dto;

import java.time.LocalDateTime;

public class OrderDataRequestDTO {

    private LocalDateTime orderDate;

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
