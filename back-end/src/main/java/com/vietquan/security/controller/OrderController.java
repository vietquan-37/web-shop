package com.vietquan.security.controller;

import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.request.PlaceOrderRequest;
import com.vietquan.security.response.OrderResponse;
import com.vietquan.security.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final OrderService service;

    @PostMapping("/placeOrder")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.placeOrder(request));
    }

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Page<OrderRequest>> getAllOrder(@RequestParam(required = false) List<OrderStatus> status, @RequestParam(defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(service.getAllPlaceOrder(status,page));
    }

    @GetMapping("/{orderId}/{orderStatus}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Integer orderId, @PathVariable String orderStatus) {
        return ResponseEntity.ok(service.changeOrderStatus(orderId, orderStatus));
    }

    @GetMapping("/myOrder/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Page<OrderRequest>>getOrderForUser(@PathVariable Integer userId,@RequestParam(required = false) List<OrderStatus> status,@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(service.getUserOrder(userId,status,page));
    }
//    @GetMapping("filter")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<List<OrderRequest>> filterOrder(@RequestParam List<OrderStatus> status) {
//        return ResponseEntity.ok(service.filterByOrderStatus(status));
//    }

}
