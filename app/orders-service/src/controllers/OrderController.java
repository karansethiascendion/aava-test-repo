package com.ecommerce.orders.controllers;

import com.ecommerce.orders.models.Order;
import com.ecommerce.orders.models.OrderCreate;
import com.ecommerce.orders.models.OrderUpdate;
import com.ecommerce.orders.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Order>> listOrders(@RequestParam(required = false) String customerId,
                                                  @RequestParam(required = false) String status,
                                                  @RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(orderService.listOrders(customerId, status, limit));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Order> createOrder(@RequestBody OrderCreate orderCreate) {
        return ResponseEntity.status(201).body(orderService.createOrder(orderCreate));
    }

    @GetMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PutMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Order> updateOrder(@PathVariable String orderId, @RequestBody OrderUpdate orderUpdate) {
        return ResponseEntity.ok(orderService.updateOrder(orderId, orderUpdate));
    }

    @DeleteMapping("/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
