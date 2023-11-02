package com.vietquan.security.service;

import com.vietquan.security.entity.*;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.repository.*;
import com.vietquan.security.request.AddProductToCartRequest;
import com.vietquan.security.request.CartItemsRequest;
import com.vietquan.security.request.OrderRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final CartItemsRepository repository;
    @Autowired
    private final OrderRepository orderRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final CouponRepository couponRepository;

    public ResponseEntity<?> addProductToCart(AddProductToCartRequest request) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);
        Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserId(request.getProductId(), activeOrder.getId(), request.getUserId());
        if (optionalCartItems.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } else {
            Optional<Product> product = productRepository.findById(request.getProductId());
            Optional<User> user = userRepository.findById(request.getUserId());
            if (product.isPresent() && user.isPresent()) {
                CartItems cartItems = new CartItems();
                cartItems.setProduct(product.get());
                cartItems.setUser(user.get());
                cartItems.setOrder(activeOrder);
                cartItems.setPrice(product.get().getPrice());
                cartItems.setQuantity(1);
                repository.save(cartItems);
                if(activeOrder.getCoupon()!=null){
                    activeOrder.setAmount(activeOrder.getAmount() + (cartItems.getPrice()*(activeOrder.getCoupon().getDiscount()/100)));
                    activeOrder.setDiscount(activeOrder.getDiscount()+(cartItems.getPrice() * (activeOrder.getCoupon().getDiscount() / 100)));
                }else{
                    activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());
                }
                activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
                activeOrder.getCarts().add(cartItems);
                orderRepository.save(activeOrder);
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product added successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User or product not found"));
            }
        }
    }
    public ResponseEntity<?> deleteProductToCart(Integer userId,Integer productId){
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserId(productId, activeOrder.getId(), userId);
        Optional<Product> product = productRepository.findById(productId);
        if (optionalCartItems.isPresent()) {
            var newTotal=activeOrder.getTotalAmount()-product.get().getPrice();
            activeOrder.setTotalAmount(newTotal);
            if (activeOrder.getCoupon() != null) {
                var newDiscount = newTotal * (activeOrder.getCoupon().getDiscount() / 100);
                activeOrder.setDiscount(newDiscount);
                activeOrder.setAmount(newTotal - newDiscount);
            }
            else {
                activeOrder.setAmount(newTotal);
            }
            orderRepository.save(activeOrder);
            repository.delete(optionalCartItems.get());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Product delete successfully"));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","User or product not found"));
        }
    }
    public OrderRequest getCartByUserId(Integer id) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(id, OrderStatus.PENDING);
        OrderRequest dto = new OrderRequest();
        List<CartItemsRequest> cartItems = activeOrder.getCarts().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        dto.setAmount(activeOrder.getAmount());
        dto.setId(activeOrder.getId());
        dto.setDiscount(activeOrder.getDiscount());
        dto.setTotalAmount(activeOrder.getTotalAmount());
        dto.setCarts(cartItems);
        if (activeOrder.getCoupon() != null) {
            dto.setCouponName(activeOrder.getCoupon().getCouponName());
        }
        return dto;


    }


    public OrderRequest applyCoupon(Integer userId, String code) throws ValidationException {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("coupon not found"));
        if (!isCouponExpired(coupon)) {
            throw new ValidationException("coupon has expired");

        }
        double discountAmount = (coupon.getDiscount() / 100) * activeOrder.getTotalAmount();
        double netAmount = activeOrder.getTotalAmount() - discountAmount;
        activeOrder.setDiscount(discountAmount);
        activeOrder.setAmount(netAmount);
        activeOrder.setCoupon(coupon);
        orderRepository.save(activeOrder);
        return activeOrder.getOrderDto();
    }


    private boolean isCouponExpired(Coupon coupon) {
        Date currentDate = new Date();
        Date expiredDate = coupon.getExpiredDate();
        if (currentDate.after(expiredDate) || expiredDate == null) {
            return false;
        }
        return true;

    }
}
