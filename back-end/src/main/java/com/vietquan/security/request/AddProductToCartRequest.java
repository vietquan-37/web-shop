package com.vietquan.security.request;

import lombok.Data;

@Data
public class AddProductToCartRequest {
    private Integer userId;
    private Integer productId;

}
