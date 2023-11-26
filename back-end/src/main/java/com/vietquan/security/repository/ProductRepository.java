package com.vietquan.security.repository;

import com.vietquan.security.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer>, PagingAndSortingRepository<Product,Integer> {

    Page<Product> findByNameContainingIgnoreCase( String name,Pageable pageable);

    Page<Product> findAll(Pageable pageable);


}

