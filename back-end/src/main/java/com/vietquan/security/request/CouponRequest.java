package com.vietquan.security.request;

import lombok.Data;

import java.util.Date;
@Data
public class CouponRequest {
    private Integer id;
    private String couponName;
    private String code;
    private Double discount;
    private Date expiredDate;
}
