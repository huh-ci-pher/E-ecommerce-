package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductRequest;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.model.Product;
import jakarta.validation.Valid;

public interface ProductService {
    ProductRequest addProduct(@Valid Product product, Long categoryId);

    ProductResponse getAllProduct();

    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchByKeyword(String keyword);

    ProductRequest updateProduct(@Valid Product product, Long productId);

    ProductRequest deleteProduct(Long productId);
}
