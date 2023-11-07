import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { PublicService } from "../../../services/public.service";
import { MatSnackBar } from "@angular/material/snack-bar";
import { CustomerService } from "../../service/customer.service";
import {Observable, range} from "rxjs";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  products: any[] = [];
  searchForm!: FormGroup;
  currentPage: number = 0;
  totalPages: number = 0;
  selectedSize: string = '';
  pageNumbers: number[] = [];
  constructor(
    private pService: PublicService,
    public service: CustomerService,
    private builder: FormBuilder,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit() {
    this.searchForm = this.builder.group({
      name: [null]
    });
    this.getAllProducts();
  }

  getAllProducts() {
    this.pService.getAllProduct(this.currentPage).subscribe((res: any) => {
      this.products = res.content.map((product: any) => ({
        processedImg: 'data:image/jpeg;base64,' + product.image,
        name: product.name,
        description: product.description,
        price: product.price,
        categoryName: product.categoryName,
        id: product.id,
        productSizes: product.productSizes

      }));

      this.totalPages = res.totalPages;
      this.updatePageNumbers();
    });
  }
  submitForm(event: any) {
    if (this.searchForm.valid) {
      const name = this.searchForm.get('name')?.value;

      if (name) {
        this.currentPage = 0; // Reset currentPage to 0 when performing a new search
        this.getAllProductsByName(name, this.currentPage);
      } else {
        this.getAllProducts();
      }
    } else {
      event.preventDefault();
    }
  }

  getAllProductsByName(name: string, page: number) {
    this.pService.getAllProductByName(name, page).subscribe((res: any) => {
      this.products = res.content.map((product: any) => ({
        processedImg: 'data:image/jpeg;base64,' + product.image,
        name: product.name,
        description: product.description,
        price: product.price,
        categoryName: product.categoryName,
        id: product.id,
        productSizes: product.productSizes
      }));
      this.totalPages = res.totalPages;
      this.updatePageNumbers();
    }, (error) => {
      this.snackBar.open('No records found.', 'Close', { duration: 5000 });
    });
  }


  addToCart(id: any, selectedSize: any) {

    this.service.addToCart(id, selectedSize).subscribe(
      (res) => {
        this.snackBar.open('Add to cart successfully', 'Close', { duration: 5000 });
      },
      (error) => {

        console.error('Error adding to cart:', error);
        if (error.status === 400) {
          this.snackBar.open('out of stock', 'Close', { duration: 5000 });
        }
        console.error('Error adding to cart:', error);
        if (error.status === 500) {
          this.snackBar.open(' please select the size', 'Close', { duration: 5000 });
        }
        else {
          this.snackBar.open('Error adding to cart. Please try again later.', 'Close', { duration: 5000 });
        }
      }
    );
  }


  loadNextPage() {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      const name = this.searchForm.get('name')?.value;
      if (name) {
        this.getAllProductsByName(name, this.currentPage);
      } else {
        this.getAllProducts();
      }
    }
  }

  loadPreviousPage() {
    if (this.currentPage > 0) {
      this.currentPage--;
      const name = this.searchForm.get('name')?.value;
      if (name) {
        this.getAllProductsByName(name, this.currentPage);
      } else {
        this.getAllProducts();
      }
    }
  }
  // Inside your component class
  loadPage(pageIndex: number) {
    if (pageIndex >= 0 && pageIndex < this.totalPages) {
      this.currentPage = pageIndex;
      const name = this.searchForm.get('name')?.value;
      if (name) {
        this.getAllProductsByName(name, this.currentPage);
      } else {
        this.getAllProducts();
      }
    }
  }


  updatePageNumbers() {
    this.pageNumbers = Array.from({ length: this.totalPages }, (_, index) => index + 1);
  }


}
