package com.vietquan.security.repository;

import com.vietquan.security.entity.Product;
import com.vietquan.security.entity.ProductSize;
import com.vietquan.security.enumPackage.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductSizeRepository extends JpaRepository<ProductSize,Integer> {
   ProductSize findBySizeAndProductProductId(Size size,Integer productId);


    Optional<ProductSize> findByProductAndSize(Product product, Size size);
}
