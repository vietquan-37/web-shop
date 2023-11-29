package com.vietquan.security.controller;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.vietquan.security.service.PayPalService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    private final PayPalService service;

    @GetMapping("/cancel")
    public ResponseEntity<String> cancelPayment() {
        service.cancelPayment();

        return ResponseEntity.ok("Payment canceled.");
    }


    @GetMapping("/success")
   public ResponseEntity<String> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
         try {
             Payment payment = service.executePayment(paymentId, payerId);
             if (payment.getState().equals("approved")) {
                 service.completed(payment);
                 return ResponseEntity.ok("Payment successful.");
             }
         } catch (PayPalRESTException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing payment.");
         }
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment not approved.");
     }


}

