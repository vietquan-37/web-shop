package com.vietquan.security.request;

import com.vietquan.security.enumPackage.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PlaceOrderRequest {

    private Integer userId;

    private String address;
    @NotBlank
    @NotBlank
    @Pattern(regexp = "\\d{10,11}", message = "Phone number must have 10-11 numeric characters")
    private String phoneNumber;


    private String orderDescription;
    private PaymentMethod method;
    private String transactionId;
}
