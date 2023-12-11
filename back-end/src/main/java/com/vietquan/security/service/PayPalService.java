package com.vietquan.security.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.vietquan.security.entity.CartItems;
import com.vietquan.security.entity.Order;
import com.vietquan.security.entity.ProductSize;
import com.vietquan.security.enumPackage.OrderStatus;
import com.vietquan.security.enumPackage.PaymentMethod;
import com.vietquan.security.repository.CartItemsRepository;
import com.vietquan.security.repository.OrderRepository;
import com.vietquan.security.repository.ProductSizeRepository;
import com.vietquan.security.request.MailForOrderRequest;
import com.vietquan.security.request.PayPalPaymentDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PayPalService {
    @Autowired
    private final OrderRepository repository;
    @Autowired
    private final ProductSizeRepository productSizeRepository;
    @Autowired
    private final CartItemsRepository cartItemsRepository;
    @Value("${paypal.clientId}")
    private String clientId;
    @Value("${paypal.clientSecret}")
    private String clientSecret;
    @Autowired
    private final EmailSenderService senderService;
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
        PayerInfo payerInfo = new PayerInfo();
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
        redirectUrls.setCancelUrl("https://ecommerce-vietquan.onrender.com/payment/cancel"); // Adjust the URL based on your application's structure
        redirectUrls.setReturnUrl("https://ecommerce-vietquan.onrender.com/payment/success"); // Adjust the URL based on your application's structure
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


    public void cancelPayment() {
        Order order = repository.findByOrderStatus(OrderStatus.PENDING);
        order.setTransactionId(null);
        repository.save(order);
    }

    public void completed(Payment payment) {
        // Extract payer information from the payment object
        PayerInfo payerInfo = payment.getPayer().getPayerInfo();
        Order order = repository.findByTransactionId(payment.getId());
        order.setPhoneNumber(order.getUser().getPhoneNumber());
        ShippingAddress shippingAddress = payerInfo.getShippingAddress();
        String formattedAddress = formatAddress(shippingAddress);
        order.setAddress(formattedAddress);
        order.setPayed(true);
        Date currentDate = new Date(System.currentTimeMillis());
        order.setDate(currentDate);
        order.setTrackingId(UUID.randomUUID());
        order.setPayment(PaymentMethod.PAYPAL);
        order.setOrderStatus(OrderStatus.PLACED);
        order.setOrderDescription("order of "+order.getUser().getFirstname()+ " " + order.getUser().getLastname());
        for (CartItems cartItem : order.getCarts()) {
            ProductSize productSize = cartItem.getProductSize();
            int remainingQuantity = productSize.getQuantity() - cartItem.getQuantity();
            productSize.setQuantity(remainingQuantity);
            productSizeRepository.save(productSize);

        }


        repository.save(order);
        Order newOrder = new Order();
        newOrder.setAmount(0.0);
        newOrder.setTotalAmount(0.0);
        newOrder.setDiscount(0.0);
        newOrder.setUser(order.getUser());
        newOrder.setOrderStatus(OrderStatus.PENDING);
        newOrder.setPayed(false);
        repository.save(newOrder);
        MailForOrderRequest forOrderRequest =new MailForOrderRequest();
        forOrderRequest.setToEmail(order.getUser().getEmail());
        forOrderRequest.setBody("Thank for placing an order of our shop,your order will be shipped after 2-3 business day (have paid by paypal)" );
        forOrderRequest.setSubject("Order Placing response");
        senderService.setMailSender(forOrderRequest);
    }

    private String formatAddress(ShippingAddress shippingAddress) {
        if (shippingAddress == null) {
            return ""; // or handle as needed
        }

        StringBuilder formattedAddress = new StringBuilder();
        formattedAddress.append(shippingAddress.getLine1());
        if (shippingAddress.getLine2() != null) {
            formattedAddress.append(", ").append(shippingAddress.getLine2());
        }
        formattedAddress.append(", ").append(shippingAddress.getCity());
        formattedAddress.append(", ").append(shippingAddress.getState());
        formattedAddress.append(", ").append(shippingAddress.getPostalCode());
        formattedAddress.append(", ").append(shippingAddress.getCountryCode());

        return formattedAddress.toString();
    }
}
