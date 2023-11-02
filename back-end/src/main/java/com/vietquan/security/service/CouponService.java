package com.vietquan.security.service;

import com.vietquan.security.entity.Coupon;
import com.vietquan.security.entity.Product;
import com.vietquan.security.repository.CouponRepository;
import com.vietquan.security.request.CouponRequest;
import com.vietquan.security.request.ProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    @Autowired
    private final CouponRepository repository;

    public CouponRequest createCoupon(CouponRequest request) {
        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode());
        coupon.setCouponName(request.getCouponName());
        coupon.setDiscount(request.getDiscount());
        coupon.setExpiredDate(request.getExpiredDate());
        if (repository.existsByCode(coupon.getCode())) {
            throw new DuplicateKeyException("code already exist");
        } else {

            return repository.save(coupon).getDto();
        }
    }
    public List<CouponRequest> getAllCoupon() {
        List<Coupon> coupons = repository.findAll();

        return coupons.stream().map(Coupon::getDto).collect((Collectors.toList()));
    }

}
