package com.vietquan.security.repository;

import com.vietquan.security.entity.Order;
import com.vietquan.security.enumPackage.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> , PagingAndSortingRepository<Order,Integer> {
    Order findByUserIdAndOrderStatus(Integer userId, OrderStatus orderStatus);

    Order findByTransactionId(String id);

    Page<Order> findAllByOrderStatusIn(List<OrderStatus> status, Pageable pageable);


    Page<Order> findByUserIdAndOrderStatusIn(Integer userId, List<OrderStatus> statuses, Pageable pageable);

    Order findByOrderStatus(OrderStatus status);


    Order findByOrderStatusAndId(OrderStatus status, Integer orderId);


   int countOrderByOrderStatus(OrderStatus statuses);

    @Query(
            value = "SELECT COALESCE(SUM(o.amount), 0) FROM Order o WHERE o.orderStatus = :status or o.isPayed=true"
    )
    Double getRevenue(@Param("status") OrderStatus status);
    @Query(
            value = "SELECT COUNT(DISTINCT o.user) FROM Order o WHERE o.orderStatus = :status OR o.isPayed = true"
    )
    int getClient(@Param("status") OrderStatus status);


}
