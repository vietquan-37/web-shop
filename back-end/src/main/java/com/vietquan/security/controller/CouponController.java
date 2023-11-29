package com.vietquan.security.controller;

import com.vietquan.security.request.CouponRequest;
import com.vietquan.security.response.ResponseMessage;
import com.vietquan.security.service.CouponService;
import jakarta.validation.Valid;
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
    public ResponseEntity<CouponRequest> createCoupon(@Valid @RequestBody CouponRequest request){
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

    @PutMapping("/update/{couponId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> createCoupon(@PathVariable Integer couponId, @Valid @RequestBody CouponRequest request) {
        return service.updateCoupon(couponId, request);
    }
    @DeleteMapping("/delete/{couponId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseMessage> deleteCoupon(@PathVariable Integer couponId) {
        return service.deleteCoupon(couponId);
    }
}
