package com.ecommerce.project.service;

import com.ecommerce.project.dto.ProductRequest;
import com.ecommerce.project.dto.ProductResponse;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductRequest addProduct(Product product, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        product.setImage("default.png");
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
        product.setSpecialPrice(specialPrice);
        product.setCategory(category);
        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductRequest.class);
    }

    @Override
    public ProductResponse getAllProduct() {
        List<Product> listOfProducts = productRepository.findAll();
        List<ProductRequest> productRequests = listOfProducts.stream()
                .map(p -> modelMapper.map(p, ProductRequest.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productRequests);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        List<Product> listOfProducts = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductRequest> productRequests = listOfProducts.stream()
                .map(p -> modelMapper.map(p, ProductRequest.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productRequests);
        return productResponse;
    }

    @Override
    public ProductResponse searchByKeyword(String keyword) {
        List<Product> listOfProducts = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductRequest> productRequests = listOfProducts.stream()
                .map(p -> modelMapper.map(p, ProductRequest.class)).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productRequests);
        return productResponse;
    }

    @Override
    public ProductRequest updateProduct(Product product, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setDiscount(product.getDiscount());
        existingProduct.setQuantity(product.getQuantity());
        if (product.getDiscount() != null) {
            double specialPrice =
                    product.getPrice() - ((product.getDiscount() / 100.0) * product.getPrice());
            existingProduct.setSpecialPrice(specialPrice);
        } else {
            existingProduct.setSpecialPrice(product.getSpecialPrice());
        }
        Product savedProduct = productRepository.save(existingProduct);
        return modelMapper.map(savedProduct, ProductRequest.class);
    }

    @Override
    public ProductRequest deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ProductId", productId));
        productRepository.delete(product);
        return modelMapper.map(product, ProductRequest.class);

    }
}



