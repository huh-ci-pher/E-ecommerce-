package com.ecommerce.project.controller;

import com.ecommerce.project.dto.ProductRequest;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductRequest> addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) {
        ProductRequest productRequest = productService.addProduct(product, categoryId);
        return new ResponseEntity<>(productRequest, HttpStatus.CREATED);
    }
}
