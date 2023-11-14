package com.vietquan.security.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.vietquan.security.entity.Order;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.repository.OrderRepository;
import com.vietquan.security.request.PayPalPaymentDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PayPalService {
    private final OrderRepository repository;
    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.clientSecret}")
    private String clientSecret;

    public PayPalPaymentDetails processPayment(double amount, String currency, String description) throws PayPalRESTException {
        Payment payment = createPayment(amount, currency, description);
        PayPalPaymentDetails details = new PayPalPaymentDetails();
        String approvalUrl = null;
        String transactionId = null;

        for (Links link : payment.getLinks()) {
            if ("approval_url".equals(link.getRel())) {
                approvalUrl = link.getHref();
                details.setApprovalUrl(approvalUrl);
            }
        }


        transactionId = payment.getId();
        details.setTransactionId(transactionId);


        return details;
    }

    private Payment createPayment(double amount, String currency, String description) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setIntent("sale");

        Payer payer = new Payer();
        payer.setPaymentMethod("PAYPAL");
        payment.setPayer(payer);

        Transaction transaction = new Transaction();
        Amount amountDetails = new Amount();
        amountDetails.setCurrency(currency);
        amountDetails.setTotal(String.format("%.2f", amount));
        transaction.setAmount(amountDetails);
        transaction.setDescription(description);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/payment/cancel"); // Adjust the URL based on your application's structure
        redirectUrls.setReturnUrl("http://localhost:8080/payment/success"); // Adjust the URL based on your application's structure
        payment.setRedirectUrls(redirectUrls);

        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        APIContext apiContext = new APIContext(clientId, clientSecret, "sandbox");
        return payment.execute(apiContext, paymentExecute);
    }


    public void updateOrderStatus(String id) {
        Order order = repository.findByTransactionId(id);
        order.setOrderStatus(OrderStatus.PAID);
        repository.save(order);
        Order newOrder = new Order();
        newOrder.setAmount(0.0);
        newOrder.setTotalAmount(0.0);
        newOrder.setDiscount(0.0);
        newOrder.setUser(order.getUser());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        repository.save(newOrder);
    }

}
