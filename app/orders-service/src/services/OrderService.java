package com.ecommerce.orders.services;

import com.ecommerce.orders.models.Order;
import com.ecommerce.orders.models.OrderCreate;
import com.ecommerce.orders.models.OrderUpdate;
import com.ecommerce.orders.models.OrderItem;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {
    private final Map<String, Order> orderRepo = new HashMap<>();

    public List<Order> listOrders(String customerId, String status, Integer limit) {
        List<Order> orders = new ArrayList<>(orderRepo.values());
        if (customerId != null) {
            orders.removeIf(o -> !customerId.equals(o.getCustomerId()));
        }
        if (status != null) {
            orders.removeIf(o -> !status.equals(o.getStatus()));
        }
        return orders.subList(0, Math.min(limit, orders.size()));
    }

    public Order createOrder(OrderCreate orderCreate) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setCustomerId(orderCreate.getCustomerId());
        order.setItems(orderCreate.getItems());
        order.setStatus("pending");
        order.setCreatedAt(java.time.LocalDateTime.now());
        double total = 0;
        for (OrderItem item : orderCreate.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalAmount(total);
        orderRepo.put(order.getId(), order);
        return order;
    }

    public Order getOrder(String orderId) {
        Order order = orderRepo.get(orderId);
        if (order == null) throw new NoSuchElementException("Order not found");
        return order;
    }

    public Order updateOrder(String orderId, OrderUpdate orderUpdate) {
        Order order = getOrder(orderId);
        if (orderUpdate.getStatus() != null) order.setStatus(orderUpdate.getStatus());
        if (orderUpdate.getItems() != null) order.setItems(orderUpdate.getItems());
        return order;
    }

    public void cancelOrder(String orderId) {
        Order order = getOrder(orderId);
        order.setStatus("cancelled");
    }
}
