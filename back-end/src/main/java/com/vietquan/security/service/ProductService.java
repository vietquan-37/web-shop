package com.vietquan.security.service;

import com.vietquan.security.entity.Category;
import com.vietquan.security.entity.Product;
import com.vietquan.security.entity.ProductSize;
import com.vietquan.security.enumPackage.Size;
import com.vietquan.security.repository.CategoryRepository;
import com.vietquan.security.repository.ProductRepository;
import com.vietquan.security.repository.ProductSizeRepository;
import com.vietquan.security.request.ProductRequest;
import com.vietquan.security.request.ProductSizeRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductSizeRepository productSizeRepository;

    @Transactional(rollbackFor = Exception.class)
    public ProductRequest createProduct(ProductRequest request) throws IOException {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setImage(request.getImg().getBytes());
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("categoryId not exist"));
        product.setCategory(category);

        Product savedProduct = repository.save(product);


        List<ProductSize> productSizes = new ArrayList<>();
        for (ProductSizeRequest sizeRequest : request.getProductSizes()) {
            ProductSize productSize = new ProductSize();
            productSize.setSize(sizeRequest.getSize());
            productSize.setQuantity(sizeRequest.getQuantity());
            productSize.setProduct(savedProduct);
            productSizes.add(productSize);
        }
        savedProduct.setProductSizes(productSizes.stream().toList());

        productSizeRepository.saveAll(productSizes);

        return savedProduct.getDto();
    }

    public Page<ProductRequest> getAllProduct(int page) {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Product> products = repository.findAll(pageable);

        return products.map(Product::getDto);
    }

    public Page<ProductRequest> getAllProductByName(String name, int page) {
        Pageable pageable = PageRequest.of(page, 4);

        Page<Product> products = repository.findByNameContainingIgnoreCase(name, pageable);
        if (products.isEmpty()) {
            throw new EntityNotFoundException("Don't have any product");
        }
        return products.map(Product::getDto);
    }

    public boolean deleteProduct(Integer id) {
        Optional<Product> product = repository.findById(id);
        if (product.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        throw new EntityNotFoundException("Don't have any record");
    }
}
