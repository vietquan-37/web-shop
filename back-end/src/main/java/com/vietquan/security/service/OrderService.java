package com.vietquan.security.service;

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
import com.vietquan.security.request.MailForOrderRequest;
import com.vietquan.security.request.OrderRequest;
import com.vietquan.security.request.PayPalPaymentDetails;
import com.vietquan.security.request.PlaceOrderRequest;
import com.vietquan.security.response.OrderResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
    private final EmailSenderService senderService;
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
                    Date currentDate = new Date(System.currentTimeMillis());
                    activeOrder.setDate((currentDate));
                    activeOrder.setPayed(false);

                    orderRepository.save(activeOrder);
                    for (CartItems cartItem : activeOrder.getCarts()) {
                        ProductSize productSize = cartItem.getProductSize();
                        int remainingQuantity = productSize.getQuantity() - cartItem.getQuantity();
                        productSize.setQuantity(remainingQuantity);
                        productSizeRepository.save(productSize);

                    }
                    MailForOrderRequest forOrderRequest =new MailForOrderRequest();
                    forOrderRequest.setToEmail(activeOrder.getUser().getEmail());
                    forOrderRequest.setBody("Thank for placing an order of our shop,your order will be shipped after 2-3 business day" );
                    forOrderRequest.setSubject("Order Placing response");
                    senderService.setMailSender(forOrderRequest);
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

    public Page<OrderRequest> getAllPlaceOrder(List<OrderStatus>statuses,int page) {
        if(page<0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page,1);
        Page<Order> orders;
        if(statuses==null) {
           orders = orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED),pageable);
        }
        else {
         orders= orderRepository.findAllByOrderStatusIn(statuses,pageable);
        }
        return orders.map(Order::getOrderDto);
    }

    public OrderRequest changeOrderStatus(Integer orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isPresent() && order.get().getOrderStatus() != OrderStatus.DELIVERED) {
            Order order1 = order.get();

            if (Objects.equals(status, "SHIPPED")) {
                order1.setOrderStatus(OrderStatus.SHIPPED);
                MailForOrderRequest forOrderRequest =new MailForOrderRequest();
                forOrderRequest.setToEmail(order1.getUser().getEmail());
                forOrderRequest.setBody("Your order are now on the shipping process" );
                forOrderRequest.setSubject("Shipping response");
                senderService.setMailSender(forOrderRequest);

            } else if (order.get().getOrderStatus() != OrderStatus.PLACED) {


                if (Objects.equals(status, "DELIVERED")) {
                    order1.setOrderStatus(OrderStatus.DELIVERED);
                    Date currentDate = new Date(System.currentTimeMillis());
                    order1.setDeliveriedDate(currentDate);
                    order1.setPayed(true);
                    MailForOrderRequest forOrderRequest =new MailForOrderRequest();
                    forOrderRequest.setToEmail(order1.getUser().getEmail());
                    forOrderRequest.setBody("Your order are now delivered ,feel free to leave a review of product on our web" );
                    forOrderRequest.setSubject("Delivery response");
                    senderService.setMailSender(forOrderRequest);

                }
            }
            else {
                throw new RuntimeException("When order status are placed cannot change to delivered");
            }
            return orderRepository.save(order1).getOrderDto();
        } else {
            throw new EntityNotFoundException("no record not able to change");
        }
    }

    public Page<OrderRequest> getUserOrder(Integer userId,List<OrderStatus>statuses,int page) {
        if(page<0){
            page=1;
        }
        Pageable pageable= PageRequest.of(page,1);
        Page<Order> orders;
        if(statuses==null) {
            orders = orderRepository.findByUserIdAndOrderStatusIn(userId,List.of(OrderStatus.PLACED, OrderStatus.SHIPPED, OrderStatus.DELIVERED),pageable);
        }
        else {
            orders= orderRepository.findByUserIdAndOrderStatusIn(userId,statuses,pageable);
        }
        return orders.map(Order::getOrderDto);
    }
//    public List<OrderRequest>filterByOrderStatus(List<OrderStatus> status){
//        if(status.size()==0)
//        return orderRepository.findAllByOrderStatusIn(status).stream().map(Order::getOrderDto).collect(Collectors.toList());
//    }
//    public List<OrderRequest>filterByOrderStatusOfUser(List<OrderStatus> status,Integer userId){
//        return orderRepository.findAllByOrderStatusInAndUserId(status,userId).stream().map(Order::getOrderDto).collect(Collectors.toList());
//    }
}
