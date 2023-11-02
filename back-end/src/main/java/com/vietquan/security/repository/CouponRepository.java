package com.vietquan.security.repository;

import com.vietquan.security.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    boolean existsByCode(String code);

    Optional<Coupon> findByCode(String code);

}
