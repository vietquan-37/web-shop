package com.vietquan.security.request;

import com.vietquan.security.enumPackage.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    private Integer userId;
    @NotBlank
    @NotNull
    private String address;
    @NotBlank
    @NotNull
    private String phoneNumber;
    @NotBlank
    @NotNull

    private String orderDescription;
    private PaymentMethod method;
    private String transactionId;
}
