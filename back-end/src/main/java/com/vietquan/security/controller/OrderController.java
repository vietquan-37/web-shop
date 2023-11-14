package com.vietquan.security.controller;

import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.request.PlaceOrderRequest;
import com.vietquan.security.response.OrderResponse;
import com.vietquan.security.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private final OrderService service;
    @PostMapping("/placeOrder")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.placeOrder(request));
    }


}
