package com.ra34.projecte2.dto;

import java.util.List;

public class OrderAddProductsRequestDTO {

    private List<Long> productIds;

    public List<Long> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Long> productIds) {
        this.productIds = productIds;
    }
}
