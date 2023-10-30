import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../service/customer.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  order: any;

  constructor(
    private service: CustomerService,
    private snackBar: MatSnackBar,
    private builder: FormBuilder,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    // Call the getCartByUserId method when the component initializes
    this.getCartData();
  }


  getCartData() {



      this.service.getCartByUserId().subscribe(
        (response) => {
          this.cartItems = response.carts.map((cartItem: any) => {
            return {
              processedImg: 'data:image/jpeg;base64,' + cartItem.img,
              price:cartItem.price,
              quantity: cartItem.quantity,
              productName:cartItem.productName

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



}
