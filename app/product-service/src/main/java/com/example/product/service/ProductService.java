package com.example.product.service;

import com.example.product.entity.Product;
import com.example.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> listProducts(int limit, String category, Pageable pageable) {
        if (category != null && !category.isBlank()) {
            return productRepository.findByCategory(category, pageable).getContent();
        }
        return productRepository.findAll(pageable).getContent();
    }

    public Product getProduct(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product createProduct(Product product) {
        product.setId(null);
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product update) {
        Product product = getProduct(id);
        if (update.getName() != null) product.setName(update.getName());
        if (update.getDescription() != null) product.setDescription(update.getDescription());
        if (update.getPrice() != null) product.setPrice(update.getPrice());
        if (update.getCategory() != null) product.setCategory(update.getCategory());
        if (update.getStock() != null) product.setStock(update.getStock());
        return productRepository.save(product);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
}
