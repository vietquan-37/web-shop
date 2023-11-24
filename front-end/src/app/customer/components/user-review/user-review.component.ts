import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-user-review',
  templateUrl: './user-review.component.html',
  styleUrls: ['./user-review.component.scss']
})
export class UserReviewComponent implements OnInit {
  reviews: any[] = [];

  constructor(
    private service: CustomerService,
  ) {
  }

  ngOnInit() {
    this.loadReview()
  }

  loadReview() {

    this.service.getReviewByUsed().subscribe(
      (res) => {

          this.reviews = res.map((review: any) => ({
            processedImg: 'data:image/jpeg;base64,' + review.img,
            star: review.star,
            productName: review.productName,
            comment: review.comment,
            starArray: this.createStarArray(review.star),
          }));

      },
      (error) => {

      }
    );
  }
  createStarArray(starCount: number): number[] {
    return Array.from({ length: starCount }, (_, index) => index + 1);
  }
}
