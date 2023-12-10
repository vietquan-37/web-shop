package com.vietquan.security.repository;

import com.vietquan.security.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    boolean existsByCode(String code);

    Optional<Coupon> findByCode(String code);
    List<Coupon> findAllByExpiredDateAfter(Date date);
}
