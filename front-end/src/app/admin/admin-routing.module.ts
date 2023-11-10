import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminComponent} from './admin.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {PostCategoryComponent} from "./components/post-category/post-category.component";
import {PostProductComponent} from "./components/post-product/post-product.component";
import {authGuard} from "../services/auth/auth.guard";
import {PostCouponComponent} from "./components/post-coupon/post-coupon.component";
import {CouponsComponent} from "./components/coupons/coupons.component";
import {roleGuard} from "../services/auth/role.guard";

const routes: Routes = [{path: '', component: AdminComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [authGuard,roleGuard]},
  {path: 'category', component: PostCategoryComponent, canActivate: [authGuard,roleGuard],},
  {path: 'product', component: PostProductComponent, canActivate: [authGuard,roleGuard],},
  {path: 'post-coupon', component: PostCouponComponent, canActivate: [authGuard,roleGuard],},
  {path: 'coupons', component: CouponsComponent, canActivate: [authGuard,roleGuard],}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
