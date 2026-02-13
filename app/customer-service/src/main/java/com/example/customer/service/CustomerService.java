package com.example.customer.service;

import com.example.customer.entity.Customer;
import com.example.customer.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;

    public List<Customer> listCustomers(int limit, Pageable pageable) {
        return customerRepository.findAll(pageable).getContent();
    }

    public Customer getCustomer(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    public Customer createCustomer(Customer customer) {
        customer.setId(null);
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(String id, Customer update) {
        Customer customer = getCustomer(id);
        if (update.getName() != null) customer.setName(update.getName());
        if (update.getEmail() != null) customer.setEmail(update.getEmail());
        if (update.getPhone() != null) customer.setPhone(update.getPhone());
        if (update.getAddress() != null) customer.setAddress(update.getAddress());
        return customerRepository.save(customer);
    }

    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
}
