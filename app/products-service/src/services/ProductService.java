package com.ecommerce.products.services;

import com.ecommerce.products.models.Product;
import com.ecommerce.products.models.ProductCreate;
import com.ecommerce.products.models.ProductUpdate;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ProductService {
    private final Map<String, Product> productRepo = new HashMap<>();

    public List<Product> listProducts(Integer limit, String category) {
        // Filtering and limiting logic
        List<Product> products = new ArrayList<>(productRepo.values());
        if (category != null) {
            products.removeIf(p -> !category.equals(p.getCategory()));
        }
        return products.subList(0, Math.min(limit, products.size()));
    }

    public Product createProduct(ProductCreate productCreate) {
        Product product = new Product();
        product.setId(UUID.randomUUID().toString());
        product.setName(productCreate.getName());
        product.setDescription(productCreate.getDescription());
        product.setPrice(productCreate.getPrice());
        product.setCategory(productCreate.getCategory());
        product.setStock(productCreate.getStock());
        product.setCreatedAt(java.time.LocalDateTime.now());
        productRepo.put(product.getId(), product);
        return product;
    }

    public Product getProduct(String productId) {
        Product product = productRepo.get(productId);
        if (product == null) throw new NoSuchElementException("Product not found");
        return product;
    }

    public Product updateProduct(String productId, ProductUpdate productUpdate) {
        Product product = getProduct(productId);
        if (productUpdate.getName() != null) product.setName(productUpdate.getName());
        if (productUpdate.getDescription() != null) product.setDescription(productUpdate.getDescription());
        if (productUpdate.getPrice() != null) product.setPrice(productUpdate.getPrice());
        if (productUpdate.getCategory() != null) product.setCategory(productUpdate.getCategory());
        if (productUpdate.getStock() != null) product.setStock(productUpdate.getStock());
        return product;
    }

    public void deleteProduct(String productId) {
        productRepo.remove(productId);
    }
}
