package com.ra34.projecte2.mapper;

import com.ra34.projecte2.dto.OrderItemResponseDTO;
import com.ra34.projecte2.dto.OrderResponseDTO;
import com.ra34.projecte2.model.Order;
import com.ra34.projecte2.model.OrderItem;

import java.math.BigDecimal;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setCustomerId(order.getCustomer().getId());
        response.setOrderDate(order.getOrderDate());
        response.setOrderStatus(order.getOrderStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderItems(order.getOrderItems().stream()
                .map(OrderMapper::toOrderItemResponseDTO)
                .toList());
        return response;
    }

    public static OrderItemResponseDTO toOrderItemResponseDTO(OrderItem item) {
        OrderItemResponseDTO response = new OrderItemResponseDTO();
        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());

        BigDecimal lineTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        response.setLineTotal(lineTotal);
        return response;
    }
}
