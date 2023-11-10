import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {authGuard} from "../services/auth/auth.guard";
import {CartComponent} from "./components/cart/cart.component";
import {roleGuard} from "../services/auth/role.guard";


const routes: Routes = [{ path: '', component: CustomerComponent },
  { path: 'dashboard', component: DashboardComponent , canActivate: [authGuard,roleGuard] },
  { path: 'cart', component: CartComponent , canActivate: [authGuard,roleGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
