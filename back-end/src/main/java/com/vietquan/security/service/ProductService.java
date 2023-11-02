package com.vietquan.security.service;

import com.vietquan.security.entity.Category;
import com.vietquan.security.entity.Product;
import com.vietquan.security.repository.CategoryRepository;
import com.vietquan.security.repository.ProductRepository;
import com.vietquan.security.request.ProductRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired
    private final ProductRepository repository;
    @Autowired
    private final CategoryRepository categoryRepository;

    public ProductRequest createProduct(ProductRequest request) throws IOException {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImage(request.getImg().getBytes());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("categoryId not exist"));

        product.setCategory(category);


        return repository.save(product).getDto();


    }

    public Page<ProductRequest> getAllProduct(int page) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<Product> products = repository.findAll(pageable);

        return products.map(Product::getDto);
    }

    public Page<ProductRequest> getAllProductByName(String name,int page) {
        Pageable pageable = PageRequest.of(page, 6);

        Page<Product> products = repository.findByNameContainingIgnoreCase(name, pageable);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Dont have any product");
        }
        return products.map(Product::getDto);
    }

    public boolean deleteProduct(Integer id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            repository.deleteById(id);

            return true;

        }
        throw new EntityNotFoundException("dont have any record");

    }
}
