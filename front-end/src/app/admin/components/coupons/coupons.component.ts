import {Component, OnInit} from '@angular/core';
import {PublicService} from "../../../services/public.service";
import {AdminService} from "../../service/admin.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {UpdateModalComponent} from "../update-modal/update-modal.component";
import {DialogConfig} from "@angular/cdk/dialog";
import {EditCouponsComponent} from "../edit-coupons/edit-coupons.component";

@Component({
  selector: 'app-coupons',
  templateUrl: './coupons.component.html',
  styleUrls: ['./coupons.component.scss']
})
export class CouponsComponent implements OnInit {
  coupons: any[] = [];

  constructor(
              public AdminService: AdminService,
              public snackBar: MatSnackBar,
              private dialog: MatDialog,
  ) {
  }

  ngOnInit() {
    this.getAllCoupons()
  }

  getAllCoupons() {
    this.AdminService.getAllCoupon().subscribe((res) => {
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

  deleteCoupon(couponId: any) {
    this.AdminService.deleteCoupon(couponId).subscribe((res) => {
        if (res != null) {
          this.snackBar.open('Delete coupon successfully', 'Close', {duration: 5000})
        }
      },
      (error) => {
        this.snackBar.open('Delete coupon unsuccessfully', 'Close', {duration: 5000})
      }
    )

  }
  updateCoupon(coupon:any){

      const dialogConfig:any = new MatDialogConfig();
      dialogConfig.data = { coupon };

      const dialogRef = this.dialog.open(EditCouponsComponent, dialogConfig);

      dialogRef.afterClosed().subscribe(result => {
        setTimeout(() => {
          window.location.reload();
        },500);
      });

  }
}
