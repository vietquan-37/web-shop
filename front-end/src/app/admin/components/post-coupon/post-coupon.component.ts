import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CustomerService} from "../../../customer/service/customer.service";
import {Router} from "@angular/router";
import {AdminService} from "../../service/admin.service";
import {error} from "@angular/compiler-cli/src/transformers/util";

@Component({
  selector: 'app-post-coupon',
  templateUrl: './post-coupon.component.html',
  styleUrls: ['./post-coupon.component.scss']
})
export class PostCouponComponent implements OnInit{
  couponForm!:FormGroup;

  constructor(private builder:FormBuilder,
              private snackBar:MatSnackBar,
              private service:AdminService,
              private router:Router) {
  }
ngOnInit() {
    this.couponForm=this.builder.group({
      couponName:[null,[Validators.required]],
      code:[null,[Validators.required]],
      discount:[null,[Validators.required]],
      expiredDate:[null,[Validators.required]]
    })
}
  addCoupon() {
    if (this.couponForm.valid) {
      this.service.addCoupon(this.couponForm.value).subscribe(
        (res) => {
          if (res.id != null) {
            this.snackBar.open("Add coupon successfully", 'Close', {duration: 5000});
            this.router.navigate(['/admin/coupons'])

          } else {
            this.snackBar.open("Add coupon unsuccessfully", 'Close', {duration: 5000});
          }
        }


        ,
        (err) => {
          if (err.status === 409) {
            this.snackBar.open("Code already exists", 'Close', {duration: 5000});
          } else {
            // Handle other errors if needed
            this.snackBar.open("An error occurred", 'Close', {duration: 5000});
          }
        }
      );
    }
    else{
      this.couponForm.markAllAsTouched();
    }

  }

}
