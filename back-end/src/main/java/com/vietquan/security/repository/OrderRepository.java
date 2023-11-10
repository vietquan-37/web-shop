package com.vietquan.security.repository;

import com.vietquan.security.entity.Order;
import com.vietquan.security.enumPackage.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByUserIdAndOrderStatus(Integer userId, OrderStatus orderStatus);

}