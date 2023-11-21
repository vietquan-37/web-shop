import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {PublicService} from "../../../services/public.service";
import {CustomerService} from "../../service/customer.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss']
})
export class ProductDetailsComponent implements OnInit{
  product: any;

  currentMainImage: string = '';
  selectedSize: string = '';

  constructor(

    public service: CustomerService,
    private builder: FormBuilder,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    const routeParams = this.route.snapshot.paramMap;
    const productIdFromRoute = Number(routeParams.get('id'));
    this.service.getProductById(productIdFromRoute).subscribe(
      (res) => {
        this.product = {
          processedImg: 'data:image/jpeg;base64,' + res.image,
          byteImages: res.byteImages.map((img: any) => 'data:image/jpeg;base64,' + img), // New line
          name: res.name,
          description: res.description,
          price: res.price,
          categoryName: res.categoryName,
          id: res.id,
          productSizes: res.productSizes
        };
        },
      (error) => {
        console.error('Error loading product:', error);
      }
    );

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
  setMainImage(image: string) {
    this.currentMainImage = image;
  }

}
