package com.vietquan.security.repository;

import com.vietquan.security.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>, PagingAndSortingRepository<Review, Integer> {

    List<Review> findByProductReviewProductIdAndOrdersId(Integer product, Integer orderId);

    Review findByProductReviewProductIdAndOrdersIdAndUsersId(Integer product, Integer orderId, Integer userId);


    Page<Review> findByProductReviewProductId(Integer product, Pageable pageable);

    Page<Review> findByUsersId(Integer ID, Pageable pageable);

}
