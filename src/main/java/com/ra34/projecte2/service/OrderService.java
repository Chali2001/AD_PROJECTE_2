package com.ra34.projecte2.service;

import com.ra34.projecte2.dto.OrderCreateRequestDTO;
import com.ra34.projecte2.dto.OrderAddProductsRequestDTO;
import com.ra34.projecte2.dto.OrderProductRequestDTO;
import com.ra34.projecte2.dto.OrderResponseDTO;
import com.ra34.projecte2.mapper.OrderMapper;
import com.ra34.projecte2.model.Customer;
import com.ra34.projecte2.model.Order;
import com.ra34.projecte2.model.OrderItem;
import com.ra34.projecte2.model.OrderStatus;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.repository.CustomerRepository;
import com.ra34.projecte2.repository.OrderRepository;
import com.ra34.projecte2.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderCreateRequestDTO request) {
        if (request == null || request.getCustomerId() == null
                || request.getProducts() == null || request.getProducts().isEmpty()) {
            return null;
        }

        Customer customer = customerRepository.findById(request.getCustomerId()).orElse(null);
        if (customer == null) {
            return null;
        }

        Order order = new Order();
        LocalDateTime now = LocalDateTime.now();

        order.setCustomer(customer);
        order.setOrderDate(request.getOrder() != null && request.getOrder().getOrderDate() != null
                ? request.getOrder().getOrderDate()
                : now);
        order.setOrderStatus(OrderStatus.PENDENT);
        order.setStatus(true);
        order.setDataCreated(now);
        order.setDataUpdated(now);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // Se crea un OrderItem por cada producto y se acumula el total automáticamente.
        for (OrderProductRequestDTO productRequest : request.getProducts()) {
            if (productRequest == null || productRequest.getProductId() == null) {
                return null;
            }

            Product product = productRepository.findById(productRequest.getProductId()).orElse(null);
            if (product == null || product.getPrice() == null) {
                return null;
            }

            int quantity = productRequest.getQuantity() == null ? 1 : productRequest.getQuantity();
            if (quantity <= 0) {
                return null;
            }

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
            totalAmount = totalAmount.add(lineTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(unitPrice);
            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO processOrder(Long orderId) {
        if (orderId == null) {
            return null;
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getOrderStatus() != OrderStatus.PENDENT) {
            return null;
        }

        order.setOrderStatus(OrderStatus.PROCESSAT);
        order.setDataUpdated(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(Long orderId) {
        if (orderId == null) {
            return null;
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null || order.getOrderStatus() != OrderStatus.PENDENT) {
            return null;
        }

        order.setOrderStatus(OrderStatus.CANCELAT);
        order.setDataUpdated(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponseDTO(savedOrder);
    }

    @Transactional
    public OrderResponseDTO addProducts(Long orderId, OrderAddProductsRequestDTO request) {
        if (orderId == null || request == null || request.getProductIds() == null || request.getProductIds().isEmpty()) {
            return null;
        }

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            return null;
        }

        BigDecimal totalAmount = order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount();

        for (Long productId : request.getProductIds()) {
            if (productId == null) {
                return null;
            }

            Product product = productRepository.findById(productId).orElse(null);
            if (product == null || product.getPrice() == null) {
                return null;
            }

            BigDecimal unitPrice = BigDecimal.valueOf(product.getPrice());
            totalAmount = totalAmount.add(unitPrice);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(1);
            orderItem.setUnitPrice(unitPrice);
            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(totalAmount);
        order.setDataUpdated(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return OrderMapper.toResponseDTO(savedOrder);
    }
}
