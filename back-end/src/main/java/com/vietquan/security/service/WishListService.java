package com.vietquan.security.service;

import com.vietquan.security.entity.Product;
import com.vietquan.security.entity.User;
import com.vietquan.security.entity.WishList;
import com.vietquan.security.repository.ProductRepository;
import com.vietquan.security.repository.UserRepository;
import com.vietquan.security.repository.WishListRepository;
import com.vietquan.security.response.ResponseMessage;
import com.vietquan.security.response.WishListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {
    @Autowired
    private final WishListRepository repository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ProductRepository productRepository;

    public Page<WishListResponse> getAllWishListByUser(Integer userId, int page) {
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, 4);
        Page<WishList> wishLists = repository.findAllByUserId(userId, pageable);
        List<WishListResponse> responses = new ArrayList<>();
        for (WishList wishList : wishLists) {
            WishListResponse response = new WishListResponse();
            response.setId(wishList.getId());
            response.setProductId(wishList.getProduct().getProductId());
            response.setPrice(wishList.getProduct().getPrice());
            response.setProductImg(wishList.getProduct().getImage());
            response.setProductName(wishList.getProduct().getName());
            responses.add(response);
        }
        return new PageImpl<>(responses, pageable, wishLists.getTotalElements());


    }

    public ResponseEntity<ResponseMessage> addOrDeleteProduct(Integer userId, Integer productId) {
        Optional<WishList> wishList = repository.findByUserIdAndProductProductId(userId, productId);

        if (wishList.isEmpty()) {

            WishList newWishList = new WishList();

            Optional<Product> product = productRepository.findById(productId);
            Optional<User> user = userRepository.findById(userId);

            if (product.isPresent() && user.isPresent()) {
                newWishList.setProduct(product.get());
                newWishList.setUser(user.get());
                repository.save(newWishList);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(ResponseMessage.builder().message("add product to wishlist successfully").build());
            } else {

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ResponseMessage.builder().message("Product or user not found").build());
            }
        } else {
            repository.deleteById(wishList.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage.builder().message("remove product from wishlist successfully").build());
        }
    }

    public boolean findProductInWishList(Integer userId, Integer productId) {
        Optional<WishList> wishList = repository.findByUserIdAndProductProductId(userId, productId);
        if (wishList.isPresent()) {
            return true;
        }
        return false;
    }
}
