// user-orders.component.ts

import { Component, OnInit } from '@angular/core';
import { CustomerService } from "../../service/customer.service";

@Component({
  selector: 'app-user-orders',
  templateUrl: './user-orders.component.html',
  styleUrls: ['./user-orders.component.scss']
})
export class UserOrdersComponent implements OnInit {
  orders: any;

  // Define arrays for different statuses
  placedOrders: any[] = [];
  shippedOrders: any[] = [];
  deliveredOrders: any[] = [];

  // Initial status selection
  selectedStatus: string = 'PLACED';
  public reviewButtonDisplayed: boolean = false;
  constructor(private service: CustomerService) {}

  ngOnInit() {
    this.getUserOrder();
  }

  getUserOrder() {
    this.service.userOrder().subscribe((response) => {
      this.orders = response;
      this.categorizeOrders();
    });
  }

  categorizeOrders() {
    // Clear existing arrays
    this.placedOrders = [];
    this.shippedOrders = [];
    this.deliveredOrders = [];

    // Categorize orders based on status
    for (const order of this.orders) {
      if (order.orderStatus === 'PLACED') {
        this.placedOrders.push(order);
      } else if (order.orderStatus === 'SHIPPED') {
        this.shippedOrders.push(order);
      } else if (order.orderStatus === 'DELIVERED') {
        this.deliveredOrders.push(order);
      }
    }
  }
}
