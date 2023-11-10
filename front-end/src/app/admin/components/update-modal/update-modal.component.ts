import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-update-modal',
  templateUrl: 'update-modal.component.html',
  styleUrls: ['./update-modal.component.scss'],
})
export class UpdateModalComponent {
  updateForm: FormGroup;
  productSizesArray: any[] = [];

  constructor(
    private dialogRef: MatDialogRef<UpdateModalComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder,
    private adminService: AdminService,
    private snackBar: MatSnackBar
  ) {
    this.updateForm = this.formBuilder.group({
      name: [data.product.name, Validators.required],
      description: [data.product.description, Validators.required],
      price: [data.product.price, Validators.required],
      productSizes: this.productSizesArray,
      disabled: true,// Initialize as an array
    });

    if (data.product.productSizes) {
      data.product.productSizes.forEach((size: any) => {
        this.productSizesArray.push(size); // Push each size object to the array
        this.updateForm.addControl(
          size.size,
          this.formBuilder.control(size.quantity,Validators.min(1))
        );
      });
    }
  }

  onSubmit() {
    if (this.updateForm.valid) {
      // Include the update for quantities in this method

      // Map quantities back to the productSizesArray
      this.productSizesArray.forEach((sizeObj: any) => {
        sizeObj.quantity = this.updateForm.get(sizeObj.size)?.value; // Update the quantity based on the form value
      });

      const updatedProduct = {
        name: this.updateForm.get('name')?.value,
        description: this.updateForm.get('description')?.value,
        price: this.updateForm.get('price')?.value,
        productSizes: this.productSizesArray, // Use the array here
      };

      this.adminService.updateProduct(this.data.product.id, updatedProduct).subscribe(
        (response: any) => {
          this.snackBar.open('Product updated successfully', 'Close', { duration: 5000 });

          this.dialogRef.close(response);
        },
        (error) => {
          this.snackBar.open('Failed to update product', 'Close', { duration: 5000 });


        }
      );
    }
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
