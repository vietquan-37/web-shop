import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PublicService} from "../../../services/public.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  products: any[] = [];
  searchForm!:FormGroup;

  constructor(private service: AdminService,
              private pService:PublicService,
              private builder: FormBuilder,
              private snackBar:MatSnackBar) {}

  ngOnInit() {
    this.getAllProducts();
    this.searchForm = this.builder.group({
      name: [null, { nonNullable: false }],
    });
  }

  getAllProducts() {
    this.pService.getAllProduct().subscribe((res) => {
      this.products = res.map((product: any) => {
        return {
          processedImg: 'data:image/jpeg;base64,' + product.image,
          name: product.name,
          description: product.description,
          price: product.price,
          categoryName: product.categoryName,
          id:product.id

        };
      });
    });
  }

  submitForm(event: any) {
    if (this.searchForm.valid) {
      const name = this.searchForm.get('name')?.value;

      if (name) {
        this.pService.getAllProductByName(name).subscribe((res) => {
          this.products = res.map((product: any) => {
            return {
              processedImg: 'data:image/jpeg;base64,' + product.image,
              name: product.name,
              description: product.description,
              price: product.price,
              categoryName: product.categoryName,
              id: product.id
            };
          });
        }, (error) => {
          this.snackBar.open('No records found.', 'Close', { duration: 5000 });
        });
      } else {
        this.getAllProducts();
      }
    } else {
      event.preventDefault();
    }
  }
  deleteProduct(id: number) {
    this.service.deleteProduct(id).subscribe(
      () => {
        this.snackBar.open('Product deleted successfully', 'Close', { duration: 5000 });
        this.getAllProducts(); // Refresh the product list after deletion
      },
      (error) => {
        this.snackBar.open('Product delete unsuccessful', 'Close', { duration: 5000 });
      }
    );
  }

}

