package com.vietquan.security.repository;

import com.vietquan.security.entity.CartItems;
import com.vietquan.security.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Integer> {

    Optional<CartItems> findByProductProductIdAndOrderIdAndUserIdAndProductSizeSizeId(Integer productId, Integer orderId, Integer userId,Integer id);
Optional<CartItems>findCartItemsByOrder(Order order);

}
