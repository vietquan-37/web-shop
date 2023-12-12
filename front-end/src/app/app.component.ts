import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from "./services/authentication.service";
import { Router, NavigationEnd } from "@angular/router";
import { CustomerService } from "./customer/service/customer.service";
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'front-end';
  coupons: any;

  constructor(
    private service: AuthenticationService,
    private router: Router,
    private CustomerService: CustomerService
  ) {}

  isCustomerLogin: boolean = this.service.isCustomerLogin();
  isAdminLogin: boolean = this.service.isAdminLogin();

  ngOnInit(): void {
    // Subscribe to NavigationEnd events using the filter operator
    this.router.events.pipe(
      filter((event): event is NavigationEnd => event instanceof NavigationEnd)
    ).subscribe((event: NavigationEnd) => {
      this.isCustomerLogin = this.service.isCustomerLogin();
      this.isAdminLogin = this.service.isAdminLogin();

      // Call getAllNonExpiredCoupon only when the user is logged in as a customer
      if (this.isCustomerLogin && event.urlAfterRedirects && event.urlAfterRedirects.startsWith('/customer')) {
        this.getAllNonExpiredCoupon();
      }
    });
  }

  logout() {
    this.service.logout().subscribe(
      () => {
        this.router.navigate(['login']);
        localStorage.clear();
      },
      error => {
        console.error('Logout error:', error);
      }
    );
  }

  getAllNonExpiredCoupon() {
    this.CustomerService.getNonExpiredCoupon().subscribe((res) => {
      this.coupons = res.map((coupon: any) => {
        return {
          code: coupon.code,
          discount: coupon.discount,
          expiredDate: coupon.expiredDate
        };
      });
    });
  }
}
