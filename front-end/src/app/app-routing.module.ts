import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {RegisterComponent} from "./register/register.component";

import {authGuard} from "./services/auth/auth.guard";
import {roleGuard} from "./services/auth/role.guard";
import {ResetComponent} from "./reset/reset.component";
import {ForgotComponent} from "./forgot/forgot.component";

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'reset', component: ResetComponent },
  { path: 'forgot', component: ForgotComponent },
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
