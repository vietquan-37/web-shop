import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";

@Component({
  selector: 'app-user-orders',
  templateUrl: './user-orders.component.html',
  styleUrls: ['./user-orders.component.scss']
})
export class UserOrdersComponent implements OnInit{
orders:any
  constructor(private service:CustomerService,
              ) {
  }
  ngOnInit() {
  this.getUserOrder()
  }
getUserOrder(){
  this.service.userOrder().subscribe((response=>{
    this.orders=response
  }))
}
}
