import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {AdminRoutingModule} from './admin-routing.module';
import {AdminComponent} from './admin.component';
import {DashboardComponent} from './components/dashboard/dashboard.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";
import {MaterialModule} from "../../material.module";
import {PostCategoryComponent} from './components/post-category/post-category.component';
import {PostProductComponent} from './components/post-product/post-product.component';
import {PostCouponComponent} from './components/post-coupon/post-coupon.component';
import {CouponsComponent} from './components/coupons/coupons.component';
import {UpdateModalComponent} from './components/update-modal/update-modal.component';
import {MatDialogModule} from "@angular/material/dialog";


@NgModule({
  declarations: [
    AdminComponent,
    DashboardComponent,
    PostCategoryComponent,
    PostProductComponent,
    PostCouponComponent,
    CouponsComponent,
    UpdateModalComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MaterialModule,
    MatDialogModule,
  ]
})
export class AdminModule { }