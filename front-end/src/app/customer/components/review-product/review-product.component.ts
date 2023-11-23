import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import { CustomerService } from "../../service/customer.service";
import { FormBuilder } from "@angular/forms";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ActivatedRoute, Router } from "@angular/router";
import { PublicService } from "../../../services/public.service";

@Component({
  selector: 'app-review-product',
  templateUrl: './review-product.component.html',
  styleUrls: ['./review-product.component.scss']
})
export class ReviewProductComponent implements OnInit{
  products: any[] = [];

  constructor(
    public service: CustomerService,
    private builder: FormBuilder,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    const routeParams = this.route.snapshot.paramMap;
    const orderIdFromRoute = Number(routeParams.get('id'));

    this.service.getProductForReview(orderIdFromRoute).subscribe(
      (res) => {
        if (res && res.productList && Array.isArray(res.productList)) {
          this.products = res.productList.map((product: any) => ({
            processedImg: 'data:image/jpeg;base64,' + product.image,
            name: product.name,
            price: product.price,
            id: product.id,
            starRating: 0, // Initialize with default value
            reviewComment: '' // Initialize with default value
          }));
        } else {
          console.error('Invalid response format:', res);
        }
      },
      (error) => {
        console.error('Error loading product:', error);
      }
    );
  }

  submitReview(productId: number, starRating: number, reviewComment: string) {
    const routeParams = this.route.snapshot.paramMap;
    const orderIdFromRoute = Number(routeParams.get('id'));
    const reviewData = {
      productId: productId,
      star: starRating,
      comment: reviewComment,
    };

    this.service.createReview(reviewData, orderIdFromRoute).subscribe(
      (res) => {
        this.snackBar.open('Review submitted successfully!', 'Close', {
          duration: 3000,
        });

        setTimeout(() => {
          window.location.reload();
        }, 2000);

      },
      (error) => {
      if(error.status==500){
        this.snackBar.open('can not review again', 'Close', {
          duration: 3000,
        });
      }
      }
    );
  }
}
