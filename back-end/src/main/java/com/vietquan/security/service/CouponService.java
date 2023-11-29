package com.vietquan.security.service;

import com.vietquan.security.entity.Coupon;
import com.vietquan.security.repository.CouponRepository;
import com.vietquan.security.request.CouponRequest;
import com.vietquan.security.response.ResponseMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    @Autowired
    private final CouponRepository repository;

    public CouponRequest createCoupon(@Valid CouponRequest request) {
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

    public ResponseEntity<ResponseMessage> updateCoupon(Integer id, CouponRequest request) {
        Optional<Coupon> coupon = repository.findById(id);
        if (coupon.isEmpty()) {
            throw new EntityNotFoundException("not found");
        }
        coupon.get().setCouponName(request.getCouponName());
        coupon.get().setCode(request.getCode());
        coupon.get().setDiscount(request.getDiscount());
        coupon.get().setExpiredDate(request.getExpiredDate());
        repository.save(coupon.get());
        return ResponseEntity.ok(ResponseMessage.builder().message("Update successfully").build());
    }
    public ResponseEntity<ResponseMessage> deleteCoupon(Integer id) {
        Optional<Coupon> coupon = repository.findById(id);
        if (coupon.isEmpty()) {
            throw new EntityNotFoundException("not found");
        }
       repository.delete(coupon.get());
        return ResponseEntity.ok(ResponseMessage.builder().message("Update successfully").build());
    }


}
