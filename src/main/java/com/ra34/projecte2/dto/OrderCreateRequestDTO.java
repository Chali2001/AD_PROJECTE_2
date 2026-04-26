package com.ra34.projecte2.dto;

import java.util.List;

public class OrderCreateRequestDTO {

    private Long customerId;
    private OrderDataRequestDTO order;
    private List<OrderProductRequestDTO> products;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public OrderDataRequestDTO getOrder() {
        return order;
    }

    public void setOrder(OrderDataRequestDTO order) {
        this.order = order;
    }

    public List<OrderProductRequestDTO> getProducts() {
        return products;
    }

    public void setProducts(List<OrderProductRequestDTO> products) {
        this.products = products;
    }
}
