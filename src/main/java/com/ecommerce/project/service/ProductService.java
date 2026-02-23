package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductRequest;
import com.ecommerce.project.model.Product;
import jakarta.validation.Valid;

public interface ProductService {
    ProductRequest addProduct(@Valid Product product, Long categoryId);
}
