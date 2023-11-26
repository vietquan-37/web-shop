import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {PublicService} from "../../../services/public.service";

@Component({
  selector: 'app-product-review',
  templateUrl: './product-review.component.html',
  styleUrls: ['./product-review.component.scss']
})
export class ProductReviewComponent implements OnInit{
  reviews: any[] = [];
  currentPage: number = 0;

  totalPages: number = 0;

  constructor(private route: ActivatedRoute, private pService: PublicService,
              private router: Router
  ) {
  }
ngOnInit() {
    this.getAllReview()
}

  onPageChange(page: number) {
    this.currentPage = page;
    this.getAllReview();
  }

  getAllReview() {
    const routeParams = this.route.snapshot.paramMap;
    const productIdFromRoute = Number(routeParams.get('id'));

    this.pService.getAllReviewByProduct(productIdFromRoute, this.currentPage)
      .subscribe(
        (res: any) => {
          console.log('Response from backend:', res);
          this.reviews = res.content;
          this.totalPages = res.totalPages;
        },
        (error) => {
          console.error('Error fetching reviews:', error);
        }
      );

  }


  goBack() {
    this.router.navigate(['/admin/dashboard']);
  }

  createStarArray(starCount: number): number[] {
    return Array.from({length: starCount}, (_, index) => index + 1);
  }
}
