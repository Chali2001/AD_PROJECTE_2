package com.ra34.projecte2.controller;

import com.ra34.projecte2.dto.OrderCreateRequestDTO;
import com.ra34.projecte2.dto.OrderAddProductsRequestDTO;
import com.ra34.projecte2.dto.OrderResponseDTO;
import com.ra34.projecte2.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Crea una comanda amb els seus order_items i calcula el total automàticament.
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderCreateRequestDTO request) {

        OrderResponseDTO response = orderService.createOrder(request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Procesa un order pendiente y devuelve la información actualizada.
    @PutMapping("/{id}/processar")
    public ResponseEntity<OrderResponseDTO> processOrder(@PathVariable Long id) {

        OrderResponseDTO response = orderService.processOrder(id);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Cancela un order pendiente y devuelve la información actualizada.
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable Long id) {

        OrderResponseDTO response = orderService.cancelOrder(id);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Añade uno o varios productos a un order existente y recalcula el total.
    @PutMapping("/{id}/products")
    public ResponseEntity<OrderResponseDTO> addProducts(@PathVariable Long id, @RequestBody OrderAddProductsRequestDTO request) {

        OrderResponseDTO response = orderService.addProducts(id, request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }
}