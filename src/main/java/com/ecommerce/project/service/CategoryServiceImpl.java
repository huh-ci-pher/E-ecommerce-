package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.dto.CategoryRequest;
import com.ecommerce.project.dto.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository repository;

    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //Sorting categories
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = repository.findAll(pageDetails);

        List<Category> listOfCategories = categoryPage.getContent();
        if (listOfCategories.isEmpty())
            throw new APIException("Category not created till now!!");

        List<CategoryRequest> categoryReq = listOfCategories.stream()
                .map(category -> modelMapper.map(category, CategoryRequest.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryReq);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryRequest createCategory(CategoryRequest categoryRequest) {
        Category mapCategory = modelMapper.map(categoryRequest, Category.class);
        Category savedCategory = repository.findByCategoryName(mapCategory.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category with the name " + mapCategory.getCategoryName() + " already exists");
        }
        Category saveCategory = repository.save(mapCategory);
        return modelMapper.map(saveCategory, CategoryRequest.class);

    }


    @Override
    public CategoryRequest deleteCategoryById(Long categoryId) {
        Category category = repository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category",
                        "categoryId", categoryId));
        repository.delete(category);
        return modelMapper.map(category, CategoryRequest.class);
    }

    @Override
    public CategoryRequest updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category updatedCategory =
                repository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Category mapCategory = modelMapper.map(categoryRequest, Category.class);
        updatedCategory.setCategoryName(mapCategory.getCategoryName());
        repository.save(updatedCategory);
        return modelMapper.map(updatedCategory, CategoryRequest.class);
    }
}

