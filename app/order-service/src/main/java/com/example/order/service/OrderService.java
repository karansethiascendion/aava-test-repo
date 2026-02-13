package com.example.order.service;

import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    public List<Order> listOrders(String customerId, String status, int limit, Pageable pageable) {
        if (customerId != null && status != null) {
            return orderRepository.findByCustomerIdAndStatus(customerId, status, pageable).getContent();
        } else if (customerId != null) {
            return orderRepository.findByCustomerId(customerId, pageable).getContent();
        } else if (status != null) {
            return orderRepository.findByStatus(status, pageable).getContent();
        } else {
            return orderRepository.findAll(pageable).getContent();
        }
    }

    public Order getOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
    }

    public Order createOrder(Order order) {
        order.setId(null);
        if (order.getItems() != null) {
            order.getItems().forEach(item -> item.setOrder(order));
        }
        return orderRepository.save(order);
    }

    public Order updateOrder(String id, Order update) {
        Order order = getOrder(id);
        if (update.getStatus() != null) order.setStatus(update.getStatus());
        if (update.getItems() != null) {
            order.getItems().clear();
            update.getItems().forEach(item -> {
                item.setOrder(order);
                order.getItems().add(item);
            });
        }
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }
}
