import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {AdminService} from "../../service/admin.service";

@Component({
  selector: 'app-post-product',
  templateUrl: './post-product.component.html',
  styleUrls: ['./post-product.component.scss']
})
export class PostProductComponent implements OnInit {
  productForm!: FormGroup;
  listOfCategories: any[] = [];
  selectedFile: File | null = null; // Provide an initial value of null
  imagePreview: string | ArrayBuffer | null = null; // Provide an initial value of null

  constructor(
    private builder: FormBuilder,
    private snackBar: MatSnackBar,
    private router: Router,
    private service: AdminService
  ) {
  }

  OnFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      const allowedExtensions = /(\.png|\.jpg|\.jpeg)$/i;
      if (!allowedExtensions.test(file.name)) {
        // Invalid file type, show an error message or handle it as needed
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

    });
    this.getAllCategory()

  }

  getAllCategory() {
    this.service.getAllCategory().subscribe((res: any[]) => {
      console.log(res); // Log the API response to check if it contains the categories data
      this.listOfCategories = res;
    });
  }

  addProduct(): void {
    if (this.productForm.invalid) {
      for (const i in this.productForm.controls) {
        this.productForm.controls[i].markAsDirty();
        this.productForm.controls[i].updateValueAndValidity();
      }
    } else {
      const formData: FormData = new FormData();
      const file = this.selectedFile || '';
      formData.append('img', file);
      formData.append('categoryId', this.productForm.get('categoryId')?.value)
      formData.append('name', this.productForm.get('name')?.value)
      formData.append('price', this.productForm.get('price')?.value)
      formData.append('description', this.productForm.get('description')?.value)
      this.service.addProduct(formData).subscribe((res) => {
        if (res.categoryId != null) {
          this.snackBar.open('Product create successfully', 'Close', {duration: 5000})

          this.router.navigateByUrl('/admin/dashboard');
        } else {
          this.snackBar.open('Product create unsuccessfully', 'Close', {duration: 5000})
        }

      })
    }


  }

}
