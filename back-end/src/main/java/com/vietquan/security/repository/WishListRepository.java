package com.vietquan.security.repository;

import com.vietquan.security.entity.WishList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer>, PagingAndSortingRepository<WishList, Integer> {
    Page<WishList> findAllByUserId(Integer userId, Pageable pageable);

    Optional<WishList> findByUserIdAndProductProductId(Integer userId, Integer productId);
}
