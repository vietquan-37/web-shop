package com.vietquan.security.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Integer reviewId;
    private Integer star;
    private String comment;
    private String username;
private Integer productId;
private String productName;
private byte[] img;
}
