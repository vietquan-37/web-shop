import {Component, OnInit} from '@angular/core';
import {PublicService} from "../../../services/public.service";

@Component({
  selector: 'app-coupons',
  templateUrl: './coupons.component.html',
  styleUrls: ['./coupons.component.scss']
})
export class CouponsComponent implements OnInit {
  coupons: any[] = [];

  constructor(private service: PublicService,
  ) {
  }

  ngOnInit() {
    this.getAllProducts()
  }

  getAllProducts() {
    this.service.getAllCoupon().subscribe((res) => {
      this.coupons = res.map((coupon: any) => {
        return {
          id: coupon.id,
          couponName: coupon.couponName,
          code: coupon.code,
          discount: coupon.discount,
          expiredDate: coupon.expiredDate

        };
      });
    });
  }
}
