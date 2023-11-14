package com.vietquan.security.request;

import com.vietquan.security.enumPackage.PaymentMethod;
import lombok.Data;

@Data
public class PlaceOrderRequest {
    private Integer userId;
    private String address;
    private String phoneNumber;
    private String orderDescription;
    private PaymentMethod method;
    private String transactionId;
}
