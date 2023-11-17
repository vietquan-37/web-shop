package com.vietquan.security.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vietquan.security.entity.CartItems;
import com.vietquan.security.entity.Order;
import com.vietquan.security.entity.ProductSize;
import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.PaymentMethod;
import com.vietquan.security.repository.OrderRepository;
import com.vietquan.security.repository.ProductSizeRepository;
import com.vietquan.security.repository.UserRepository;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.request.PayPalPaymentDetails;
import com.vietquan.security.request.PlaceOrderRequest;
import com.vietquan.security.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final PayPalService service;
@Autowired
private final ProductSizeRepository productSizeRepository;
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        // Find the active order for the user
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);

        Optional<User> user = userRepository.findById(request.getUserId());

        // Check if the user exists
        if (user.isPresent()) {
            // Update the active order details
            if (request.getMethod() == PaymentMethod.PAYPAL) {
                // For PayPal, set the order status to PENDING and initiate the PayPal payment
                activeOrder.setOrderStatus(OrderStatus.PENDING);

                try {
                    // Process the PayPal payment
                    PayPalPaymentDetails payment = service.processPayment(activeOrder.getAmount(), "USD", request.getOrderDescription());

                    // Update the order with the PayPal transaction ID
                    activeOrder.setTransactionId(payment.getTransactionId());
                    activeOrder.setPayed(false);
                    // Save the updated active order
                    orderRepository.save(activeOrder);

                    // Build and return the OrderResponse with the PayPal approval URL
                    return OrderResponse.builder().approvalUrl(payment.getApprovalUrl()).request(activeOrder.getOrderDto()).build();
                } catch (PayPalRESTException e) {
                    // Handle PayPal payment exception
                    e.printStackTrace();
                    // Optionally, you may want to handle this exception more gracefully
                }
            } else if (request.getMethod() == PaymentMethod.COD) {
                if (request.getAddress() == null || request.getPhoneNumber() == null || request.getOrderDescription() == null) {
                    throw new IllegalArgumentException("Please input all the required fields for COD orders.");
                } else {
                    activeOrder.setOrderDescription(request.getOrderDescription());
                    activeOrder.setOrderStatus(OrderStatus.PLACED);
                    activeOrder.setAddress(request.getAddress());
                    activeOrder.setTrackingId(UUID.randomUUID());
                    activeOrder.setPhoneNumber(request.getPhoneNumber());
                    activeOrder.setPayment(request.getMethod());
                    activeOrder.setPayed(false);

                    orderRepository.save(activeOrder);
                    for (CartItems cartItem : activeOrder.getCarts()) {
                        ProductSize productSize = cartItem.getProductSize();
                        int remainingQuantity = productSize.getQuantity() - cartItem.getQuantity();
                        productSize.setQuantity(remainingQuantity);
                        productSizeRepository.save(productSize);

                    }
                }

                // Create a new pending order for the user
                Order newOrder = new Order();
                newOrder.setAmount(0.0);
                newOrder.setTotalAmount(0.0);
                newOrder.setDiscount(0.0);
                newOrder.setUser(user.get());
                newOrder.setOrderStatus(OrderStatus.PENDING);
                newOrder.setPayed(false);
                orderRepository.save(newOrder);

                // Return OrderResponse without PayPal approval URL
                return OrderResponse.builder().request(activeOrder.getOrderDto()).build();
            }

            // Handle other payment methods as needed

        }

        return null;
    }
}
