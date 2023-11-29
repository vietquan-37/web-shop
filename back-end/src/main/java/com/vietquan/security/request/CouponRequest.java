package com.vietquan.security.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
public class CouponRequest {
    private Integer id;
    @NotBlank
    @NonNull
    private String couponName;
    @NotBlank
    @NonNull
    private String code;
    @Min(value = 10)
    @Max(value = 75)
    private Double discount;
    private Date expiredDate;
}
