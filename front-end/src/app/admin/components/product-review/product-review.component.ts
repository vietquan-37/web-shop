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

    this.route.queryParams.subscribe(params => {
      this.currentPage = +params['page'] || 0;
      if (this.currentPage < 0) {
        this.currentPage = 0
      }
      this.getAllReview();
    });
  }

  onPageChange(page: number) {

    this.currentPage = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {  page: this.currentPage },
      queryParamsHandling: 'merge'
    });

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
          if(res.content==''){
            if(this.currentPage>0){
              this.router.navigate([], {

                relativeTo: this.route,
                queryParams: { page: res.totalPages-1},
                queryParamsHandling: 'merge',
              });
            }


          }

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
