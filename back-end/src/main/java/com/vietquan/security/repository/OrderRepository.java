package com.vietquan.security.repository;

import com.vietquan.security.entity.Order;
import com.vietquan.security.enumPackage.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
    Order findByUserIdAndOrderStatus(Integer userId, OrderStatus orderStatus);
Order findByTransactionId(String id);
Order findByOrderStatus(OrderStatus status);
List<Order> findAllByOrderStatusIn(List<OrderStatus> status);
List<Order>findByUserIdAndOrderStatusIn(Integer userId,List<OrderStatus>statuses);


}
