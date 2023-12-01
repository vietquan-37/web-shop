package com.vietquan.security.controller;

import com.vietquan.security.response.ResponseMessage;
import com.vietquan.security.response.WishListResponse;
import com.vietquan.security.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishList")
@RequiredArgsConstructor
public class WishListController {
    @Autowired
    private final WishListService service;
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<Page<WishListResponse>>getAllWishListByUser(@PathVariable Integer userId, @RequestParam(defaultValue = "0")int page){
       return ResponseEntity.ok(service.getAllWishListByUser(userId,page));
    }
    @PostMapping("/{userId}/{productId}")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResponseMessage>addOrRemoveWishList(@PathVariable Integer userId, @PathVariable Integer productId){
    return service.addOrDeleteProduct(userId,productId);
    }
    @GetMapping("/check/{userId}/{productId}")
    public ResponseEntity<Boolean> findProductInWishlist(
            @PathVariable Integer userId,
            @PathVariable Integer productId) {

        boolean isProductInWishlist = service.findProductInWishList(userId, productId);

        return ResponseEntity.ok(isProductInWishlist);
    }

}
