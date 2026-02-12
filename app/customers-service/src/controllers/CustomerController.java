package com.ecommerce.customers.controllers;

import com.ecommerce.customers.models.Customer;
import com.ecommerce.customers.models.CustomerCreate;
import com.ecommerce.customers.models.CustomerUpdate;
import com.ecommerce.customers.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<Customer>> listCustomers(@RequestParam(required = false, defaultValue = "20") Integer limit) {
        return ResponseEntity.ok(customerService.listCustomers(limit));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Customer> createCustomer(@RequestBody CustomerCreate customerCreate) {
        return ResponseEntity.status(201).body(customerService.createCustomer(customerCreate));
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Customer> getCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(customerService.getCustomer(customerId));
    }

    @PutMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Customer> updateCustomer(@PathVariable String customerId, @RequestBody CustomerUpdate customerUpdate) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customerUpdate));
    }

    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
