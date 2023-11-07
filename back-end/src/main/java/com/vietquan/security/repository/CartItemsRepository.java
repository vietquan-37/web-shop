package com.vietquan.security.repository;

import com.vietquan.security.entity.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {

    Optional<CartItems> findByProductProductIdAndOrderIdAndUserIdAndProductSizeSizeId(Integer productId, Integer orderId, Integer userId,Integer id);



}
