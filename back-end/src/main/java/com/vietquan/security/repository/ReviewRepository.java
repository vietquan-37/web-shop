package com.vietquan.security.repository;

import com.vietquan.security.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProductReviewProductIdAndOrdersId(Integer product, Integer orderId);
    Review findByProductReviewProductIdAndOrdersIdAndUsersId(Integer product, Integer orderId,Integer userId);
    List<Review> findByProductReviewProductId(Integer product);

}
