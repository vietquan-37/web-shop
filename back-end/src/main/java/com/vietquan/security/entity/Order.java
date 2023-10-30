package com.vietquan.security.entity;

import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @OneToOne
    @JoinColumn(
            referencedColumnName = "Id",
            name = "user_id"
    )
    User user;
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "order"
    )
    private List<CartItems> carts;
}
