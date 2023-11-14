package com.vietquan.security.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayPalPaymentDetails {
    private String approvalUrl;
    private String transactionId;
}
