package com.vietquan.security.service;

import com.vietquan.security.entity.Category;
import com.vietquan.security.entity.Product;
import com.vietquan.security.repository.CategoryRepository;
import com.vietquan.security.repository.ProductRepository;
import com.vietquan.security.request.ProductRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<ProductRequest> getAllProduct() {
        List<Product> products = repository.findAll();

        return products.stream().map(Product::getDto).collect((Collectors.toList()));
    }

    public List<ProductRequest> getAllProductByName(String name) {
        List<Product> products = repository.findAllByNameContaining(name);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Dont have any product");
        }
        return products.stream().map(Product::getDto).collect((Collectors.toList()));
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
