package com.vietquan.security.controller;

import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.request.ReviewRequest;
import com.vietquan.security.response.ProductForReviewResponse;
import com.vietquan.security.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private final ReviewService service;

    @PostMapping("/create/{orderId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ReviewRequest> createReview(@RequestBody ReviewRequest request, @PathVariable Integer orderId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReview(request,orderId));
    }

    @GetMapping("/get/{productId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Page<ReviewRequest>>getAllReviewById(@PathVariable Integer productId, @RequestParam(defaultValue = "0")int page) {
        return ResponseEntity.ok(service.getAllReviewByProduct(productId,page));
    }
    @GetMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ProductForReviewResponse>getAllProductForReview(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.getProductForReview(orderId));
    }
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Page<ReviewRequest>>getReviewByUserId(@PathVariable Integer userId,@RequestParam(defaultValue = "0")int page) {
        return ResponseEntity.ok(service.getAllReviewByUserId(userId,page));
    }
}
