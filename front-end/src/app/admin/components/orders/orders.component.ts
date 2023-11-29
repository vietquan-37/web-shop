// orders.component.ts
import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit {
  orders: any[]=[];
  orderStatusFilter: string = '';
  orderStatusOptions: string[] = ['PLACED', 'SHIPPED', 'DELIVERED'];
  currentPage = 0;
  totalPages: number = 0;

  constructor(private service: AdminService, private snackBar: MatSnackBar,private route: ActivatedRoute, private router: Router
  ) {}

  ngOnInit() {
    // Khi trang được tải, kiểm tra URL để lấy giá trị của tham số
    this.route.queryParams.subscribe(params => {

      this.orderStatusFilter = params['status'] || '';

      this.currentPage = +params['page'] || 0;
      if(this.currentPage<0){
        this.currentPage=0
      }
      this.getPlaceOrders();
    });
  }

//...

  onStatusFilterChange() {

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { status: this.orderStatusFilter, page: 0 },
      queryParamsHandling: 'merge',
    });
    this.getPlaceOrders();
  }

  onPageChange(page: number) {

    this.currentPage = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { status: this.orderStatusFilter, page: this.currentPage },
      queryParamsHandling: 'merge',
    });

    this.getPlaceOrders();
  }

  changeOrderStatus(orderId: number, orderStatus: any) {
    this.service.changeOrderStatus(orderId, orderStatus).subscribe(
      (response) => {
        if (response.id != null) {
          this.snackBar.open('Update order status successfully', 'Close', { duration: 5000 });

          setTimeout(() => {
            window.location.reload();
          }, 1000);
        } else {
          this.snackBar.open('Some error occurred during updating status', 'Close', { duration: 5000 });
        }
      },
      (error) => {
        if (error.status == 404) {
          this.snackBar.open('Product shipping stage is complete, cannot change', 'Close', { duration: 5000 });
        }
        if (error.status == 500) {
          this.snackBar.open('When order status is placed, cannot change to delivered', 'Close', { duration: 5000 });
        }
      }
    );
  }

  getPlaceOrders() {
    const statusFilter: any[] = this.orderStatusFilter ? [this.orderStatusFilter] : [];

    this.service.getAllOrder(statusFilter, this.currentPage).subscribe((response: any) => {

      this.orders = response.content;
      this.totalPages = response.totalPages;
      if(response.content==''){
        if(this.currentPage>0){
        this.router.navigate([], {

          relativeTo: this.route,
          queryParams: { status: this.orderStatusFilter, page: response.totalPages-1},
          queryParamsHandling: 'merge',
        });
          }


      }


    });
  }



}
