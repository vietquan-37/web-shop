import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../service/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  order: any;
  couponForm!:FormGroup


  constructor(
    private service: CustomerService,
    private snackBar: MatSnackBar,
    private builder: FormBuilder,
    private dialog: MatDialog

  ) {}

  ngOnInit() {
    this.couponForm=this.builder.group({
      code:[null,[Validators.required]]
    })
    // Call the getCartByUserId method when the component initializes
    this.getCartData();
  }
  applyCoupon(){

    this.service.applyCoupon(this.couponForm.get(['code'])?.value).subscribe((res)=>{
      this.snackBar.open('Add coupon successfully','Close',{duration:5000})
        this.getCartData();
    },
      (error) =>{
      if(error.status==404){
        this.snackBar.open('code not found','Close',{duration:5000})
      }
      }
    )
  }



  getCartData() {



      this.service.getCartByUserId().subscribe(
        (response) => {
          this.cartItems = response.carts.map((cartItem: any) => {
            return {
              productId:cartItem.productId,
              processedImg: 'data:image/jpeg;base64,' + cartItem.img,
              price:cartItem.price,
              quantity: cartItem.quantity,
              productName:cartItem.productName,
              size:cartItem.size

            };
          });
          this.order = response; // Assuming there is an order object in the response
        },
        (error) => {
          console.error('Error fetching cart data:', error);
          this.snackBar.open('Error fetching cart data.', 'Close', { duration: 5000 });
        }
      );

  }
  deleteProductFromCart(productId: number,size:any) {

    this.service.deleteProductFromCart( productId,size).subscribe(
      () => {
        this.getCartData(); // Refresh cart data after deletion
        this.snackBar.open('Product removed from cart.', 'Close', { duration: 3000 });
      },
      (error) => {
        console.error('Error deleting product from cart:', error);
        this.snackBar.open('Error removing product from cart.', 'Close', { duration: 5000 });
      }
    );
  }
  increaseQuantity(productId: number,size:any) {

    this.service.increaseQuantity( productId,size).subscribe(
      () => {
        this.getCartData(); // Refresh cart data after deletion
        this.snackBar.open('Product quantity increased successfully', 'Close', { duration: 3000 });
      },
      (error) => {
        if(error.status==400){
          this.snackBar.open('dont have enough quantity','Close',{duration:5000})
        }

      }
    );
  }
  decreaseQuantity(productId: number,size:any) {

    this.service.decreaseQuantity( productId,size).subscribe(
      () => {
        this.getCartData(); // Refresh cart data after deletion
        this.snackBar.open('Product quantity decreased successfully', 'Close', { duration: 3000 });
      },
      (error) => {
        if(error.status==400){
          this.snackBar.open('error while decrease the quantity','Close',{duration:5000})
        }

      }
    );
  }






}
