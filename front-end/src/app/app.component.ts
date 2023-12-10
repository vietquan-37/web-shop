import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from "./services/authentication.service";
import {Router} from "@angular/router";
import {CustomerService} from "./customer/service/customer.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'front-end';
  coupons: any

  constructor(private service: AuthenticationService,
              private router: Router,
              private CustomerService: CustomerService) {
  }

  isCustomerLogin: boolean = this.service.isCustomerLogin();
  isAdminLogin: boolean = this.service.isAdminLogin();

  ngOnInit(): void {


    this.router.events.subscribe(event => {
      this.isCustomerLogin = this.service.isCustomerLogin();
      this.isAdminLogin = this.service.isAdminLogin();

      if (this.isCustomerLogin) {
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
        // Handle logout error if needed
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
    })
  }


}
