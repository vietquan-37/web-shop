import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";
import {CustomerComponent} from "./customer/customer.component";
import {AdminComponent} from "./admin/admin.component";
import {authGuard} from "./services/auth/auth.guard";
import {roleGuard} from "./services/auth/role.guard";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'customer',
    loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
    canActivate: [authGuard, roleGuard],
    data: { expectedRole: 'USER' } // Specify the expected role for the customer route
  },
  {
    path: 'admin',
    loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
    canActivate: [authGuard, roleGuard],
    data: { expectedRole: 'ADMIN' } // Specify the expected role for the admin route
  }
]; 

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
