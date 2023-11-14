package com.vietquan.security.service;

import com.vietquan.security.entity.*;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.Size;
import com.vietquan.security.repository.*;
import com.vietquan.security.request.AddProductToCartRequest;
import com.vietquan.security.request.CartItemsRequest;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.request.PlaceOrderRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    private final ProductSizeRepository productSizeRepository;


    public ResponseEntity<?> addProductToCart(AddProductToCartRequest request) {
        ProductSize size = productSizeRepository.findBySizeAndProductProductId(request.getSize(), request.getProductId());

        // Check if the selected size is available (quantity > 0)
        if (size != null && size.getQuantity() > 0) {
            Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);
            Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserIdAndProductSizeSizeId(request.getProductId(), activeOrder.getId(), request.getUserId(), size.getSizeId());

            if (optionalCartItems.isPresent()) {

                if (size.getQuantity() > optionalCartItems.get().getQuantity()) {
                    optionalCartItems.get().setQuantity(optionalCartItems.get().getQuantity() + 1);
                    optionalCartItems.get().setPrice(optionalCartItems.get().getPrice() + size.getProduct().getPrice());
                    repository.save(optionalCartItems.get());
                    activeOrder.setTotalAmount(activeOrder.getTotalAmount() + size.getProduct().getPrice());
                    if (activeOrder.getCoupon() != null) {
                        activeOrder.setAmount((activeOrder.getTotalAmount() * (1 - (activeOrder.getCoupon().getDiscount() / 100))));
                        activeOrder.setDiscount((activeOrder.getTotalAmount() * ((activeOrder.getCoupon().getDiscount() / 100))));
                    } else {
                        activeOrder.setAmount(activeOrder.getTotalAmount());
                    }

                    activeOrder.getCarts().add(optionalCartItems.get());
                    orderRepository.save(activeOrder);
                    return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product added successfully"));

                } else {
                    // Return a response indicating that the selected product is out of stock
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Product is out of stock"));
                }
            } else {

                if (size.getQuantity() > 0) {
                    Optional<Product> product = productRepository.findById(request.getProductId());
                    Optional<User> user = userRepository.findById(request.getUserId());
                    if (product.isPresent() && user.isPresent() || size.getQuantity() > optionalCartItems.get().getQuantity()) {
                        CartItems cartItems = new CartItems();
                        cartItems.setProduct(product.get());
                        cartItems.setUser(user.get());
                        cartItems.setOrder(activeOrder);
                        cartItems.setPrice(product.get().getPrice());
                        cartItems.setQuantity(1);
                        cartItems.setProductSize(size);
                        repository.save(cartItems);
                        if (activeOrder.getCoupon() != null) {
                            activeOrder.setAmount(activeOrder.getAmount() + (cartItems.getPrice() * (1 - (activeOrder.getCoupon().getDiscount() / 100))));
                            activeOrder.setDiscount(activeOrder.getDiscount() + (cartItems.getPrice() * ((activeOrder.getCoupon().getDiscount() / 100))));
                        } else {
                            activeOrder.setAmount(activeOrder.getAmount() + cartItems.getPrice());
                        }
                        activeOrder.setTotalAmount(activeOrder.getTotalAmount() + cartItems.getPrice());
                        activeOrder.getCarts().add(cartItems);
                        orderRepository.save(activeOrder);
                        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Product added successfully"));
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User or product not found"));
                    }
                } else {

                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Product is out of stock"));
                }
            }
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Selected size is out of stock"));
        }
    }




    public ResponseEntity<?> deleteProductToCart(Integer userId, Integer productId, Size size) {
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        ProductSize size1 = productSizeRepository.findBySizeAndProductProductId(size, productId);
        Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserIdAndProductSizeSizeId(productId, activeOrder.getId(), userId, size1.getSizeId());

        if (optionalCartItems.isPresent()) {
            var newTotal = activeOrder.getTotalAmount() - optionalCartItems.get().getPrice();
            activeOrder.setTotalAmount(newTotal);
            if (activeOrder.getCoupon() != null) {
                var newDiscount = newTotal * (activeOrder.getCoupon().getDiscount() / 100);
                activeOrder.setDiscount(newDiscount);
                activeOrder.setAmount(newTotal - newDiscount);
            } else {
                activeOrder.setAmount(newTotal);
            }
            orderRepository.save(activeOrder);
            repository.delete(optionalCartItems.get());
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Product delete successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User or product not found"));
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

    public ResponseEntity<?> increaseQuantity(AddProductToCartRequest request) {
        ProductSize size = productSizeRepository.findBySizeAndProductProductId(request.getSize(), request.getProductId());
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);
        Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserIdAndProductSizeSizeId(request.getProductId(), activeOrder.getId(), request.getUserId(),size.getSizeId());

        if ( optionalCartItems.isPresent()) {
            if(size.getQuantity()>optionalCartItems.get().getQuantity()) {
                CartItems items = optionalCartItems.get();
                items.setPrice(optionalCartItems.get().getPrice() + size.getProduct().getPrice());
                items.setQuantity(items.getQuantity() + 1);
                var newTotal = activeOrder.getTotalAmount() +size.getProduct().getPrice();
                activeOrder.setTotalAmount(newTotal);
                if (activeOrder.getCoupon() != null) {
                    activeOrder.setAmount(newTotal * (1 - (activeOrder.getCoupon().getDiscount() / 100)));
                    activeOrder.setDiscount(newTotal *  (activeOrder.getCoupon().getDiscount() / 100));
                } else {
                    activeOrder.setAmount(newTotal);
                }
                repository.save(items);
                orderRepository.save(activeOrder);
                return ResponseEntity.status(HttpStatus.OK).body(activeOrder.getOrderDto());
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "dont have enough quantity"));
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "not found cart"));
    }
    public ResponseEntity<?> decreaseQuantity(AddProductToCartRequest request) {
        ProductSize size = productSizeRepository.findBySizeAndProductProductId(request.getSize(), request.getProductId());
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);
        Optional<CartItems> optionalCartItems = repository.findByProductProductIdAndOrderIdAndUserIdAndProductSizeSizeId(request.getProductId(), activeOrder.getId(), request.getUserId(),size.getSizeId());

        if ( optionalCartItems.isPresent()) {
            if(size.getQuantity()>optionalCartItems.get().getQuantity()) {
                if (optionalCartItems.get().getQuantity() > 1) {
                    CartItems items = optionalCartItems.get();
                    items.setQuantity(items.getQuantity() - 1);
                    items.setPrice(optionalCartItems.get().getPrice() - size.getProduct().getPrice());
                    var newTotal = activeOrder.getTotalAmount() -size.getProduct().getPrice();
                    activeOrder.setTotalAmount(newTotal);
                    if (activeOrder.getCoupon() != null) {
                        activeOrder.setAmount(newTotal * (1 - (activeOrder.getCoupon().getDiscount() / 100)));
                        activeOrder.setDiscount(newTotal * (activeOrder.getCoupon().getDiscount() / 100));
                    } else {
                        activeOrder.setAmount(newTotal);
                    }
                    repository.save(items);
                    orderRepository.save(activeOrder);
                    return ResponseEntity.status(HttpStatus.OK).body(activeOrder.getOrderDto());
                }
                else{
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "quantity have to be large than 1 to decrease"));
                }
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "dont have enough quantity"));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "not found cart"));
    }

}

