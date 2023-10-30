import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {AdminService} from "../../../admin/service/admin.service";
import {PublicService} from "../../../services/public.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CustomerService} from "../../service/customer.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  products: any[] = [];
  searchForm!:FormGroup;

  constructor(
              private pService:PublicService,
              public service:CustomerService,
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
  addToCart(id: any) {
    this.service.addToCart(id).subscribe(
      (res) => {

      },
      (error) => {
        if (error.status === 409) {
          this.snackBar.open('The product is already in the cart', 'Close', {duration: 5000});
        }
        if (error.status === 201) {
          this.snackBar.open('Add to cart successfully', 'Close', { duration: 5000 });
        }
      },
      () => {
        this.snackBar.dismiss();
      }
    );
  }

}
