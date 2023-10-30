package com.vietquan.security.controller;

import com.vietquan.security.entity.Order;
import com.vietquan.security.request.AddProductToCartRequest;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    @Autowired
    private final CartService service;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductToCartRequest request) {
        return service.addProductToCart(request);

    }
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> getCartByUserId(@PathVariable Integer userId) {
        OrderRequest request=service.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(request);

    }
}
