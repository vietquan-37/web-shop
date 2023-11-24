package com.vietquan.security.service;

import com.vietquan.security.entity.CartItems;
import com.vietquan.security.entity.Order;
import com.vietquan.security.entity.Product;
import com.vietquan.security.entity.Review;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.repository.OrderRepository;
import com.vietquan.security.repository.ProductRepository;
import com.vietquan.security.repository.ReviewRepository;
import com.vietquan.security.request.ProductRequest;
import com.vietquan.security.request.ReviewRequest;
import com.vietquan.security.response.ProductForReviewResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository repository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public ReviewRequest createReview(ReviewRequest request,Integer orderId) {
        Order order = orderRepository.findByOrderStatusAndId(OrderStatus.DELIVERED,orderId);
        Optional<Product> productOptional = productRepository.findById(request.getProductId());
       var hehe= repository.findByProductReviewProductIdAndOrdersIdAndUsersId(productOptional.get().getProductId(),orderId,order.getUser().getId());
        if (order != null) {
            if(hehe!=null){
                throw new RuntimeException("denied review");
            }
            Product product = productOptional.orElseThrow(() -> new RuntimeException("Product not found"));

            Review review = new Review();
            review.setProductReview(product);
            review.setStar(request.getStar());
            review.setComment(request.getComment());
            review.setUsers(order.getUser());
            review.setReviewed(true);
            review.setOrders(order);
            return repository.save(review).getDto();

        } else {
            throw new RuntimeException("Cannot review the product when it has not been delivered.");
        }
    }

    public List<ReviewRequest> getAllReviewByProduct(Integer productId) {
        List<Review> review = repository.findByProductReviewProductId(productId);
        if (review.isEmpty()) {
            throw new EntityNotFoundException("not found any review");
        }
        return review.stream().map(Review::getDto).collect(Collectors.toList());
    }

    public ProductForReviewResponse getProductForReview(Integer orderId) {
        Order order = orderRepository.findByOrderStatusAndId(OrderStatus.DELIVERED, orderId);
        Set<ProductRequest> requests = new HashSet<>();

        if (order != null) {
            for (CartItems cartItems : order.getCarts()) {
                Product product = cartItems.getProduct();
                ProductRequest request = new ProductRequest();
                request.setId(product.getProductId());
                request.setImage(product.getImage());
                request.setName(product.getName());
                request.setPrice(product.getPrice());

                // Check if a review exists for the product and orderId
                var isReview = repository.findByProductReviewProductIdAndOrdersId(request.getId(), orderId);

                // If no review is found, add the product to the list
                if (isReview.isEmpty()) {
                    requests.add(request);
                }
            }

            // Create the response and set the product list
            ProductForReviewResponse response = new ProductForReviewResponse();
            response.setProductList(new ArrayList<>(requests));

            // If the product list is empty, throw an exception
            if (response.getProductList().isEmpty()) {
                throw new EntityNotFoundException("No product to review");
            }

            return response;
        }
        else{
            throw new EntityNotFoundException("not found");
        }
    }

public List<ReviewRequest> getAllReviewByUserId(Integer userId) {
    List<Review> reviews = repository.findByUsersId(userId);
    if(reviews.isEmpty()){
     throw new EntityNotFoundException("No review was found");
    }
    return reviews.stream().map(Review::getDto).collect(Collectors.toList());


}


}
