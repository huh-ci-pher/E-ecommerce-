package com.ecommerce.project.controller;

import com.ecommerce.project.dto.ProductRequest;
import com.ecommerce.project.dto.ProductResponse;
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

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts() {
        ProductResponse productResponse = productService.getAllProduct();
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId) {
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword) {
        ProductResponse productResponse = productService.searchByKeyword(keyword);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequest> updateProduct(@Valid @RequestBody Product product, @PathVariable Long productId) {
        ProductRequest productRequest = productService.updateProduct(product, productId);
        return new ResponseEntity<>(productRequest, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductRequest> deleteProduct(@PathVariable Long productId) {
        ProductRequest deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }
}
