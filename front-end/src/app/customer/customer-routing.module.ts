import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {authGuard} from "../services/auth/auth.guard";
import {CartComponent} from "./components/cart/cart.component";
import {roleGuard} from "../services/auth/role.guard";
import {UserOrdersComponent} from "./components/user-orders/user-orders.component";
import {ProductDetailsComponent} from "./components/product-details/product-details.component";
import {ReviewProductComponent} from "./components/review-product/review-product.component";
import {UserReviewComponent} from "./components/user-review/user-review.component";
import {UserProfileComponent} from "./components/user-profile/user-profile.component";
import {WishlistComponent} from "./components/wishlist/wishlist.component";


const routes: Routes = [{ path: '', component: CustomerComponent },
  { path: 'dashboard', component: DashboardComponent , canActivate: [authGuard,roleGuard] },
  { path: 'cart', component: CartComponent , canActivate: [authGuard,roleGuard]},
  { path: 'order', component: UserOrdersComponent , canActivate: [authGuard,roleGuard]},
  { path: 'product/:id', component: ProductDetailsComponent ,canActivate: [authGuard,roleGuard] },
  { path: 'review/:id', component: ReviewProductComponent, canActivate: [authGuard,roleGuard]},
  { path: 'review', component: UserReviewComponent, canActivate: [authGuard,roleGuard]},
  { path: 'profile', component: UserProfileComponent, canActivate: [authGuard,roleGuard]},
  { path: 'wishlist', component: WishlistComponent, canActivate: [authGuard,roleGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
