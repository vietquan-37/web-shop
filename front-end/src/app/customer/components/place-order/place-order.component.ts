import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {CustomerService} from "../../service/customer.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.scss']
})
export class PlaceOrderComponent {
  placeOrderForm!:FormGroup
constructor(private builder:FormBuilder,
            private snackBar:MatSnackBar,
            private dialogRef: MatDialogRef<PlaceOrderComponent>,
            @Inject(MAT_DIALOG_DATA) public data: any,
            private service:CustomerService,
private router:Router) {
  this.placeOrderForm = this.builder.group({
    orderDescription: [data.orderDescription, Validators.required],
    address: [data.address, Validators.required],
    phoneNumber: [data.phoneNumber, Validators.required],

  });


}

  onSubmit() {
    if (this.placeOrderForm.valid) {
      const OrderDto = {
        userId: this.data.orderDto.userId,
        method:this.data.orderDto.method,
        orderDescription: this.placeOrderForm.get('orderDescription')?.value,
        address: this.placeOrderForm.get('address')?.value,
        phoneNumber:this.placeOrderForm.get('phoneNumber')?.value

      };

      this.service.placeOrder(OrderDto).subscribe(
        (response: any) => {
          this.snackBar.open('Place Order successfully', 'Close', { duration: 5000 });
          this.router.routeReuseStrategy.shouldReuseRoute = () => false;
          this.router.onSameUrlNavigation = 'reload';
          this.router.navigate([this.router.url]);
          this.dialogRef.close(response);
        },
        (error) => {
          this.snackBar.open('Failed to place order', 'Close', { duration: 5000 });


        }
      );
    }
  }

}
