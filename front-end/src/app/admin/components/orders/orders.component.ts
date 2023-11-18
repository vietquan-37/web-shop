import {Component, OnInit} from '@angular/core';
import {AdminService} from "../../service/admin.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss']
})
export class OrdersComponent implements OnInit{
orders:any
  constructor(private service:AdminService,
              private snackBar:MatSnackBar ) {

  }
  ngOnInit() {
  this.getPlaceOrders()
  }

  getPlaceOrders(){
  this.service.getAllPlaceOrder().subscribe((response=>{
this.orders=response
  }))
  }
  changeOrderStatus(orderId:number,orderStatus:any){
  this.service.changeOrderStatus(orderId,orderStatus).subscribe((response=>{
    if(response.id!=null){
      this.snackBar.open('Update order status successfully','Close',{duration:5000})
      this.getPlaceOrders();
    }
    else{
      this.snackBar.open('Some error occur during updating status','Close',{duration:5000})
    }
  }))
  }
}
