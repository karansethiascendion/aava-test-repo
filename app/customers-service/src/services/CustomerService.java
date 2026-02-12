package com.ecommerce.customers.services;

import com.ecommerce.customers.models.Customer;
import com.ecommerce.customers.models.CustomerCreate;
import com.ecommerce.customers.models.CustomerUpdate;
import com.ecommerce.customers.models.Address;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CustomerService {
    private final Map<String, Customer> customerRepo = new HashMap<>();

    public List<Customer> listCustomers(Integer limit) {
        List<Customer> customers = new ArrayList<>(customerRepo.values());
        return customers.subList(0, Math.min(limit, customers.size()));
    }

    public Customer createCustomer(CustomerCreate customerCreate) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID().toString());
        customer.setName(customerCreate.getName());
        customer.setEmail(customerCreate.getEmail());
        customer.setPhone(customerCreate.getPhone());
        customer.setAddress(customerCreate.getAddress());
        customer.setCreatedAt(java.time.LocalDateTime.now());
        customerRepo.put(customer.getId(), customer);
        return customer;
    }

    public Customer getCustomer(String customerId) {
        Customer customer = customerRepo.get(customerId);
        if (customer == null) throw new NoSuchElementException("Customer not found");
        return customer;
    }

    public Customer updateCustomer(String customerId, CustomerUpdate customerUpdate) {
        Customer customer = getCustomer(customerId);
        if (customerUpdate.getName() != null) customer.setName(customerUpdate.getName());
        if (customerUpdate.getEmail() != null) customer.setEmail(customerUpdate.getEmail());
        if (customerUpdate.getPhone() != null) customer.setPhone(customerUpdate.getPhone());
        if (customerUpdate.getAddress() != null) customer.setAddress(customerUpdate.getAddress());
        return customer;
    }

    public void deleteCustomer(String customerId) {
        customerRepo.remove(customerId);
    }
}
