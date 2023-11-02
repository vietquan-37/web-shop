package com.vietquan.security.controller;

import com.vietquan.security.request.CouponRequest;
import com.vietquan.security.request.ProductRequest;
import com.vietquan.security.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/coupon")
@AllArgsConstructor
public class CouponController {
    private final CouponService service;
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CouponRequest> createCoupon(@RequestBody CouponRequest request){
        CouponRequest request1=service.createCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request1);
    }
    @GetMapping("")
    @PreAuthorize("hasAnyRole('ADMIN','USER')"
    )
    public ResponseEntity<List<CouponRequest>> getAllCoupon() {
        List<CouponRequest> all = service.getAllCoupon();
        return ResponseEntity.ok(all);
    }

}
