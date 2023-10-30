package com.vietquan.security.service;

import com.vietquan.security.entity.Category;
import com.vietquan.security.repository.CategoryRepository;
import com.vietquan.security.request.CategoryRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository repository;

    public Category createCategory(CategoryRequest request) {
        try {
            Category category = new Category();
            category.setName(request.getName());
            category.setDescription(request.getDescription());
            return repository.save(category);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            throw new RuntimeException("Failed to create category: " + e.getMessage());
        }
    }
    public List<Category> getAllCategory(){
        var category=repository.findAll();
        if (category==null){
            throw new EntityNotFoundException("dont have any category");
        }
        return repository.findAll();
    }
}
