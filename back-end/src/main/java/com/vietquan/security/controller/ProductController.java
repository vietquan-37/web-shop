package com.vietquan.security.controller;

import com.vietquan.security.request.ProductRequest;
import com.vietquan.security.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductRequest> createProduct(@Valid @ModelAttribute ProductRequest request) throws IOException {
        ProductRequest request1 = service.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request1);

    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','USER')"
    )
    public ResponseEntity<List<ProductRequest>> getAllProduct() {
        List<ProductRequest> all = service.getAllProduct();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/search/{name}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')"
    )
    public ResponseEntity<List<ProductRequest>> getAllProductByName(@PathVariable String name) {
        List<ProductRequest> all = service.getAllProductByName(name);
        return ResponseEntity.ok(all);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        boolean isDeleted = service.deleteProduct(id);
        if (
                isDeleted
        ) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
