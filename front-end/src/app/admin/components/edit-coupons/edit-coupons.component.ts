import {Component, Inject, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {AdminService} from "../../service/admin.service";

@Component({
  selector: 'app-edit-coupons',
  templateUrl: './edit-coupons.component.html',
  styleUrls: ['./edit-coupons.component.scss']
})
export class EditCouponsComponent {

couponForm!:FormGroup
  constructor(
    private snackBar:MatSnackBar,
    private dialogRef: MatDialogRef<EditCouponsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder,
    private adminService: AdminService,
  ) {
    this.couponForm=this.formBuilder.group({
      couponName: [data.coupon.couponName, Validators.required],
      code: [data.coupon.code, Validators.required],
      discount: [data.coupon.discount, [Validators.required,Validators.min(10),Validators.max(75)]],
      expiredDate:  [data.coupon.expiredDate, Validators.required],
    })
  }

  onSubmit() {
    if (this.couponForm.valid) {


      const updatedCoupon = {
        couponName: this.couponForm.get('couponName')?.value,
        code: this.couponForm.get('code')?.value,
        discount: this.couponForm.get('discount')?.value,
        expiredDate: this.couponForm.get('expiredDate')?.value, // Use the array here
      };

      this.adminService.updateCoupon(this.data.coupon.id, updatedCoupon).subscribe(
        (response: any) => {
          if (response && response.error) {

            this.snackBar.open(response.error, 'Close', { duration: 5000 });
          } else {

            this.snackBar.open('Coupon updated successfully', 'Close', { duration: 5000 });
            this.dialogRef.close(response);
          }
        },
        (error) => {

          this.snackBar.open('Failed to update Coupon', 'Close', { duration: 5000 });
        }
      );

    }
  }
}
