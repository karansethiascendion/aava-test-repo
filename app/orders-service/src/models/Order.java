package com.ecommerce.orders.models;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private String id;
    private String customerId;
    private List<OrderItem> items;
    private double totalAmount;
    private String status;
    private LocalDateTime createdAt;

    // Getters and Setters
}
