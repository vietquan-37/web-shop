package com.vietquan.security.entity;

import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.PaymentMethod;
import com.vietquan.security.request.OrderRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderDescription;
    private Date date;
    private Double amount;
    private String address;
    private Date deliveriedDate;
    @Enumerated(
            EnumType.STRING
    )
    private PaymentMethod payment;
    @Enumerated(
            EnumType.STRING
    )
    private OrderStatus orderStatus;
    private Double totalAmount;
    private Double discount;
    private UUID trackingId;
    private String phoneNumber;
    @OneToOne
    @JoinColumn(
            referencedColumnName = "Id",
            name = "user_id"
    )
    User user;
    @OneToOne
    @JoinColumn(
            referencedColumnName = "id",
            name = "coupon_id"

    )
    Coupon coupon;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<CartItems> carts;
    private String transactionId;
    private boolean isPayed;
    @OneToMany(
            mappedBy = "orders",cascade = CascadeType.ALL,orphanRemoval = true
    )
    private List<Review> reviews;
    public OrderRequest getOrderDto(){
        OrderRequest request=new OrderRequest();
        request.setId(id);
        request.setOrderDescription(orderDescription);
        request.setDate(date);
        request.setAmount(amount);
        request.setDiscount(discount);
        request.setTotalAmount(totalAmount);
        request.setOrderStatus(orderStatus);
        request.setPayment(payment);
        request.setTrackingId(trackingId);
        request.setAddress(address);
        request.setUsername(user.getUsername());
        request.setPhoneNumber(phoneNumber);
        request.setPayed(isPayed);
        if (coupon != null) {
            request.setCouponName(coupon.getCouponName());
        }
        if (carts != null) {
            request.setCarts(carts.stream().map(CartItems::getCartDto).collect(Collectors.toList()));
        }
        if(deliveriedDate!=null){
            request.setDeliveriedDate(deliveriedDate);
        }
        return request;
    }
}
