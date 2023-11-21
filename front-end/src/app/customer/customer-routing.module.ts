import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {authGuard} from "../services/auth/auth.guard";
import {CartComponent} from "./components/cart/cart.component";
import {roleGuard} from "../services/auth/role.guard";
import {UserOrdersComponent} from "./components/user-orders/user-orders.component";
import {ProductDetailsComponent} from "./components/product-details/product-details.component";


const routes: Routes = [{ path: '', component: CustomerComponent },
  { path: 'dashboard', component: DashboardComponent , canActivate: [authGuard,roleGuard] },
  { path: 'cart', component: CartComponent , canActivate: [authGuard,roleGuard]},
  { path: 'order', component: UserOrdersComponent , canActivate: [authGuard,roleGuard]},
  { path: 'product/:id', component: ProductDetailsComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
