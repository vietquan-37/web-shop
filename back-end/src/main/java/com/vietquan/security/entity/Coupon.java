package com.vietquan.security.entity;

import com.vietquan.security.request.CouponRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Table(name = "coupons")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;
    private String couponName;
    private String code;
    private Double discount;
    private Date expiredDate;

    public CouponRequest getDto(){
        CouponRequest request=new CouponRequest();
        request.setId(id);
        request.setCode(code);
        request.setCouponName(couponName);
        request.setDiscount(discount);
        request.setExpiredDate(expiredDate);
        return request;


    }
}
