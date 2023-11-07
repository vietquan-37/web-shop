package com.vietquan.security.controller;

import com.vietquan.security.enumPackage.Size;
import com.vietquan.security.request.AddProductToCartRequest;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.service.CartService;
import jakarta.xml.bind.ValidationException;
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
        OrderRequest request = service.getCartByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(request);

    }

    @PostMapping("/{userId}/{code}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> applyCode(@PathVariable Integer userId, @PathVariable String code) throws ValidationException {
        OrderRequest request = service.applyCoupon(userId, code);
        return ResponseEntity.status(HttpStatus.OK).body(request);

    }

    @DeleteMapping("/delete/{userId}/{productId}/{size}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable Integer userId, @PathVariable Integer productId,@PathVariable Size size) {
        return service.deleteProductToCart(userId, productId,size);
    }
    @PostMapping ("/increase")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> increaseQuantity(@RequestBody AddProductToCartRequest request) {
        return service.increaseQuantity(request);
    }
    @PostMapping ("/decrease")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> decreaseQuantity(@RequestBody AddProductToCartRequest request) {
        return service.decreaseQuantity(request);
    }
}
