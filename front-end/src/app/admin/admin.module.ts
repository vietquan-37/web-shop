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
import { OrdersComponent } from './components/orders/orders.component';
import { ProductReviewComponent } from './components/product-review/product-review.component';
import { UserManagementComponent } from './components/user-management/user-management.component';
import { EditCouponsComponent } from './components/edit-coupons/edit-coupons.component';


@NgModule({
  declarations: [
    AdminComponent,
    DashboardComponent,
    PostCategoryComponent,
    PostProductComponent,
    PostCouponComponent,
    CouponsComponent,
    UpdateModalComponent,
    OrdersComponent,
    ProductReviewComponent,
    UserManagementComponent,
    EditCouponsComponent
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
