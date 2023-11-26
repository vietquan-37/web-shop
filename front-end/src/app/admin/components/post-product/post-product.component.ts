import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";
import { AdminService } from "../../service/admin.service";

@Component({
  selector: 'app-post-product',
  templateUrl: './post-product.component.html',
  styleUrls: ['./post-product.component.scss']
})
export class PostProductComponent implements OnInit {
  productForm!: FormGroup;
  listOfCategories: any[] = [];
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  sizes: string[] = ['S', 'M', 'L', 'XL', 'NON_SIZE'];
  additionalImages: File[] = [];
  additionalImagePreviews: string[] = [];
  constructor(
    private builder: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private service: AdminService
  ) { }

  get productSizesFormArray() {
    return this.productForm.get('productSizes') as FormArray;
  }

  addProductSize() {
    const productSizes = this.productForm.get('productSizes') as FormArray;
    const nonSizeSelected = productSizes.controls.some(control => control.get('size')?.value === 'NON_SIZE');
    if (!nonSizeSelected) {
      productSizes.push(
        this.builder.group({
          size: ['', [Validators.required]],
          quantity: ['', [Validators.required, Validators.min(1)]]
        })
      );
    } else {
      this.snackBar.open('Cannot add other sizes when NON_SIZE is selected.', 'Close', { duration: 5000 });
    }
  }


  onAdditionalImageSelected(event: any) {
    const files = event.target.files;
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      if (file) {
        const allowedExtensions = /(\.png|\.jpg|\.jpeg)$/i;
        if (!allowedExtensions.test(file.name)) {
          this.snackBar.open('Please select a PNG or JPG file.', 'Close', { duration: 5000 });
        } else {
          this.additionalImages.push(file); // Store the selected image
          this.additionalImagePreviews.push(this.getImagePreview(file));
        }
      }
    }
  }

  getImagePreview(file: File): string {
    return URL.createObjectURL(file);
  }

  OnFileSelected(events: any) {
    const file = events.target.files[0];
    if (file) {
      const allowedExtensions = /(\.png|\.jpg|\.jpeg)$/i;
      if (!allowedExtensions.test(file.name)) {
        this.snackBar.open('Please select a PNG or JPG file.', 'Close', { duration: 5000 });
        this.selectedFile = null;
        this.imagePreview = null;
      } else {
        this.selectedFile = file;
        this.previewImage();
      }
    }
  }

  previewImage() {
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    if (this.selectedFile) {
      reader.readAsDataURL(this.selectedFile);
    }
  }

  ngOnInit() {
    this.productForm = this.builder.group({
      categoryId: [null, Validators.required],
      name: [null, Validators.required],
      price: [null, Validators.required],
      description: [null, Validators.required],
      productSizes: this.builder.array([]),
    });

    this.getAllCategory();
  }
  removeAdditionalImage(index: number) {
    // Remove the selected additional image based on the provided index
    this.additionalImages.splice(index, 1);
    this.additionalImagePreviews.splice(index, 1);
  }





  getAllCategory() {
    this.service.getAllCategory().subscribe((res: any[]) => {
      this.listOfCategories = res;
    });
  }

  addProduct(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      this.snackBar.open('Please fill out all the required fields.', 'Close', { duration: 5000 });
      return;
    }

    const formData: FormData = new FormData();
    const file = this.selectedFile || '';
    formData.append('img', file);
    formData.append('categoryId', this.productForm.get('categoryId')?.value);
    formData.append('name', this.productForm.get('name')?.value);
    formData.append('price', this.productForm.get('price')?.value);
    formData.append('description', this.productForm.get('description')?.value);

    const productSizes = this.productForm.get('productSizes') as FormArray;
    for (let i = 0; i < productSizes.length; i++) {
      formData.append(`productSizes[${i}].size`, productSizes.at(i).get('size')?.value);
      formData.append(`productSizes[${i}].quantity`, productSizes.at(i).get('quantity')?.value);
    }
    for (let i = 0; i < this.additionalImages.length; i++) {
      formData.append(`additionalImages[${i}]`, this.additionalImages[i]);
    }
    this.service.addProduct(formData).subscribe((res) => {
      if (res.categoryId != null) {
        this.snackBar.open('Product created successfully', 'Close', { duration: 5000 });
        this.router.navigateByUrl('/admin/dashboard');
      } else {
        this.snackBar.open('Product creation unsuccessful', 'Close', { duration: 5000 });
      }
    });
  }
  removeProductSize(index: number) {
    const productSizes = this.productForm.get('productSizes') as FormArray;
    productSizes.removeAt(index);
  }
}
