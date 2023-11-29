import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user-review',
  templateUrl: './user-review.component.html',
  styleUrls: ['./user-review.component.scss']
})
export class UserReviewComponent implements OnInit {
  reviews: any[] = [];
  currentPage: number = 0;
  totalPages: number = 0;

  constructor(
    private service: CustomerService,
    private route: ActivatedRoute,
    private router: Router
  ) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {


      this.currentPage = +params['page'] || 0;
      if (this.currentPage < 0) {
        this.currentPage = 0
      }
      this.loadReview();
    });
  }


  loadReview() {
    this.service.getReviewByUsed(this.currentPage).subscribe(
      (res: any) => {
        console.log('Review Response:', res);

        // Check if 'content' property exists and is an array
        if (res.content && Array.isArray(res.content)) {
          this.reviews = res.content.map((review: any) => ({
            processedImg: 'data:image/jpeg;base64,' + review.img,
            star: review.star,
            productName: review.productName,
            comment: review.comment,
            starArray: this.createStarArray(review.star),
          }));
          this.totalPages = res.totalPages;
          if (res.content == '') {
            if (this.currentPage > 0) {
              this.router.navigate([], {

                relativeTo: this.route,
                queryParams: { page: res.totalPages - 1},
                queryParamsHandling: 'merge',
              });
            }


          }

        }


      },
      (error) => {
        console.error('Error fetching reviews:', error);
      }
    );
  }


  createStarArray(starCount: number): number[] {
    return Array.from({ length: starCount }, (_, index) => index + 1);
  }
  onPageChange(page: number) {
    this.currentPage = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {page: this.currentPage},
      queryParamsHandling: 'merge',
    });
    this.loadReview();
  }
}
