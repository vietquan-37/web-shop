import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AdminComponent} from './admin.component';
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {PostCategoryComponent} from "./components/post-category/post-category.component";
import {PostProductComponent} from "./components/post-product/post-product.component";
import {authGuard} from "../services/auth/auth.guard";

const routes: Routes = [{path: '', component: AdminComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [authGuard]},
  {path: 'category', component: PostCategoryComponent, canActivate: [authGuard],},
  {path: 'product', component: PostProductComponent, canActivate: [authGuard],}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule {
}
