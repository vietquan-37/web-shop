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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


        if (request.getProductSizes().size() != 1 && request.getProductSizes().size() != 4) {
            throw new IllegalArgumentException("Invalid number of sizes. Allowed: 1 (NON_SIZE) or 4 (S, M, L, XL).");
        }


        if (request.getProductSizes().size() == 1 && request.getProductSizes().get(0).getSize() != Size.NON_SIZE) {
            throw new IllegalArgumentException("Only NON_SIZE size is allowed when inputting 1 size.");
        }

        if (request.getProductSizes().size() == 4) {
            for (ProductSizeRequest sizeRequest : request.getProductSizes()) {
                if (sizeRequest.getSize() == Size.NON_SIZE) {
                    throw new IllegalArgumentException("NON_SIZE size cannot be inputted when inputting 4 sizes.");
                }
            }
        }
        Product savedProduct = repository.save(product);

        List<ProductSize> productSizes = new ArrayList<>();
        for (ProductSizeRequest sizeRequest : request.getProductSizes()) {
            ProductSize productSize = new ProductSize();

            productSize.setSize(sizeRequest.getSize());
            if (sizeRequest.getQuantity() < 1) {
                throw new IllegalArgumentException("quantity must be larger than 1");
            }
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

    public ProductRequest getProductById(Integer id) {
        Product product = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found any product"));
        return product.getDto();

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

    public ProductRequest updateProduct(Integer productId,ProductRequest request) throws IOException {
        Product product = repository.findById(productId).orElseThrow(() -> new EntityNotFoundException("No product was found"));
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());


        for (ProductSizeRequest sizeRequest : request.getProductSizes()) {
            ProductSize productSize = productSizeRepository.findByProductAndSize(product, sizeRequest.getSize())
                    .orElseThrow(() -> new EntityNotFoundException("No product was found"));
            if (sizeRequest.getQuantity() < 1) {
                throw new IllegalArgumentException("quantity must be larger than 1");
            }
            productSize.setQuantity(sizeRequest.getQuantity());

            productSizeRepository.save(productSize);
        }



        Product savedProduct = repository.save(product);
        return savedProduct.getDto();
    }

}