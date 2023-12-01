package com.vietquan.security.response;

import lombok.Data;

@Data
public class WishListResponse {


    private Integer id;
    private Integer productId;
    private String productName;
    private Double price;
    private byte[] productImg;


}
