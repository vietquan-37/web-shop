import {Component, OnInit} from '@angular/core';
import {AdminService} from "../../service/admin.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit{
  orders: any
  orderStatusFilter: string = '';
  orderDateFilter: Date | null = null;
  constructor(private service:AdminService,
              private snackBar:MatSnackBar ) {

  }
  ngOnInit() {
  this.getPlaceOrders()
  }

  filterOrderByStatus(order: any): boolean {
    const statusMatch =
      !this.orderStatusFilter || order.orderStatus === this.orderStatusFilter;

    const dateMatch =
      !this.orderDateFilter ||
      new Date(order.orderDate).toDateString() ===
      new Date(this.orderDateFilter).toDateString();

    return statusMatch && dateMatch;
  }
  changeOrderStatus(orderId:number,orderStatus:any){
    this.service.changeOrderStatus(orderId, orderStatus).subscribe((response) => {
        if (response.id != null) {
          this.snackBar.open('Update order status successfully', 'Close', {duration: 5000})
          this.getPlaceOrders();
        } else {
          this.snackBar.open('Some error occur during updating status', 'Close', {duration: 5000})
        }

      },
      (error) => {
        if (error.status == 404) {
          this.snackBar.open('product shipping stage it complete can not change', 'Close', {duration: 5000})
        }
        if (error.status == 500) {
          this.snackBar.open('When order status are placed cannot change to delivered', 'Close', {duration: 5000})
        }
      }
    )
  }



  getPlaceOrders() {
    this.service.getAllPlaceOrder().subscribe((response: any) => {
      this.orders = response.filter((order: any) => this.filterOrderByStatus(order));
    });
  }

}
