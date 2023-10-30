package com.vietquan.security.controller;

import com.vietquan.security.entity.Category;
import com.vietquan.security.request.CategoryRequest;
import com.vietquan.security.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = service.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','USER')"
    )
    public ResponseEntity<List<Category>> getAllCategory() {
        var all = service.getAllCategory();
        return ResponseEntity.ok(all);
    }
}
