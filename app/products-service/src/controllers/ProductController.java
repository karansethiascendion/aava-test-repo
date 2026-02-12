package com.ecommerce.products.controllers;

import com.ecommerce.products.models.Product;
import com.ecommerce.products.models.ProductCreate;
import com.ecommerce.products.models.ProductUpdate;
import com.ecommerce.products.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<List<Product>> listProducts(@RequestParam(required = false, defaultValue = "20") Integer limit,
                                                      @RequestParam(required = false) String category) {
        return ResponseEntity.ok(productService.listProducts(limit, category));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductCreate productCreate) {
        return ResponseEntity.status(201).body(productService.createProduct(productCreate));
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Product> getProduct(@PathVariable String productId) {
        return ResponseEntity.ok(productService.getProduct(productId));
    }

    @PutMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable String productId, @RequestBody ProductUpdate productUpdate) {
        return ResponseEntity.ok(productService.updateProduct(productId, productUpdate));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
