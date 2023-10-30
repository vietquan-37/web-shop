import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {authGuard} from "../services/auth/auth.guard";
import {CartComponent} from "./components/cart/cart.component";


const routes: Routes = [{ path: '', component: CustomerComponent },
  { path: 'dashboard', component: DashboardComponent , canActivate: [authGuard]},
  { path: 'cart', component: CartComponent , canActivate: [authGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
