// user-orders.component.ts

import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user-orders',
  templateUrl: './user-orders.component.html',
  styleUrls: ['./user-orders.component.scss']
})
export class UserOrdersComponent implements OnInit {

  orders: any[] = [];
  orderStatusFilter: string = '';
  orderStatusOptions: string[] = ['PLACED', 'SHIPPED', 'DELIVERED'];
  currentPage:number = 0;
  totalPages: number = 0;

  constructor(private service: CustomerService,
              private route: ActivatedRoute,
              private router: Router
  ) {
  }

  ngOnInit() {
    // Khi trang được tải, kiểm tra URL để lấy giá trị của tham số
    this.route.queryParams.subscribe(params => {

      this.orderStatusFilter = params['status'] || '';

      this.currentPage = +params['page'] || 0;
      if (this.currentPage < 0) {
        this.currentPage = 0
      }
      this.getUserOrder();
    });
  }

//...

  onStatusFilterChange() {

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {status: this.orderStatusFilter, page: 0},
      queryParamsHandling: 'merge',
    });
    this.getUserOrder();
  }

  onPageChange(page: number) {

    this.currentPage = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {status: this.orderStatusFilter, page: this.currentPage},
      queryParamsHandling: 'merge',
    });

    this.getUserOrder()
  }


  getUserOrder() {
    const statusFilter: any[] = this.orderStatusFilter ? [this.orderStatusFilter] : [];
    this.service.userOrder(statusFilter, this.currentPage).subscribe((response) => {
      this.orders = response.content;
      this.totalPages = response.totalPages;
      if (response.content == '') {
        if (this.currentPage > 0) {
          this.router.navigate([], {

            relativeTo: this.route,
            queryParams: {status: this.orderStatusFilter, page: response.totalPages - 1},
            queryParamsHandling: 'merge',
          });
        }


      }
    });
  }


}
