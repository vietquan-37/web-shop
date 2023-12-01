package com.vietquan.security.request;

import lombok.Data;

@Data
public class MailForOrderRequest {
    private String toEmail;
    private String subject;
    private String body;
}
