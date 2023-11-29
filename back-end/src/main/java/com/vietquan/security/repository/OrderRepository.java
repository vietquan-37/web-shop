package com.vietquan.security.repository;

import com.vietquan.security.entity.Order;
import com.vietquan.security.enumPackage.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> , PagingAndSortingRepository<Order,Integer> {
    Order findByUserIdAndOrderStatus(Integer userId, OrderStatus orderStatus);

    Order findByTransactionId(String id);

    Page<Order> findAllByOrderStatusIn(List<OrderStatus> status, Pageable pageable);
    List<Order> findAllByOrderStatusInAndUserId(List<OrderStatus> status,Integer ID);

    Page<Order> findByUserIdAndOrderStatusIn(Integer userId, List<OrderStatus> statuses,Pageable pageable);

    Order findByOrderStatus(OrderStatus status);



    Order findByOrderStatusAndId(OrderStatus status, Integer orderId);


}
