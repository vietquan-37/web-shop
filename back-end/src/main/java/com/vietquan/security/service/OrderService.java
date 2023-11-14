package com.vietquan.security.service;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vietquan.security.entity.Order;
import com.vietquan.security.entity.User;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.PaymentMethod;
import com.vietquan.security.repository.OrderRepository;
import com.vietquan.security.repository.UserRepository;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.request.PayPalPaymentDetails;
import com.vietquan.security.request.PlaceOrderRequest;
import com.vietquan.security.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final UserRepository userRepository;
@Autowired private final PayPalService service;
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        // Find the active order for the user
        Order activeOrder = orderRepository.findByUserIdAndOrderStatus(request.getUserId(), OrderStatus.PENDING);

        Optional<User> user = userRepository.findById(request.getUserId());

        // Check if the user exists
        if (user.isPresent()) {
            // Update the active order details
            activeOrder.setOrderDescription(request.getOrderDescription());
            activeOrder.setOrderStatus(OrderStatus.PLACED);
            activeOrder.setAddress(request.getAddress());
            activeOrder.setTrackingId(UUID.randomUUID());
            activeOrder.setPayment(request.getMethod());
            activeOrder.setPhoneNumber(request.getPhoneNumber());

            if (request.getMethod() == PaymentMethod.PAYPAL) {
                try {


                    // Process the PayPal payment
                    PayPalPaymentDetails payment = service.processPayment(activeOrder.getAmount(), "USD", request.getOrderDescription());

                    // Update the order with the PayPal transaction ID
                    activeOrder.setTransactionId(payment.getTransactionId());

                    // Save the updated active order
                    orderRepository.save(activeOrder);

                    // Build and return the OrderResponse with approval URL
                    return OrderResponse.builder().approvalUrl(payment.getApprovalUrl()).request(activeOrder.getOrderDto()).build();
                } catch (PayPalRESTException e) {
                    // Handle PayPal payment exception
                    e.printStackTrace();
                }
            }

            // Save the updated active order
            orderRepository.save(activeOrder);

            // Create a new pending order for the user
            Order newOrder = new Order();
            newOrder.setAmount(0.0);
            newOrder.setTotalAmount(0.0);
            newOrder.setDiscount(0.0);
            newOrder.setUser(user.get());
            newOrder.setOrderStatus(OrderStatus.PENDING);
            orderRepository.save(newOrder);


            return OrderResponse.builder().request(activeOrder.getOrderDto()).build();
        }


        return null;
    }


}
