import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.scss']
})
export class WishlistComponent implements OnInit {
  wishList: any[] = [];
  currentPage: number = 0;
  totalPages: number = 0;

  constructor(private service: CustomerService,
              private route:ActivatedRoute,
              private router:Router
  ) {
  }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {


      this.currentPage = +params['page'] || 0;
      if (this.currentPage < 0) {
        this.currentPage = 0
      }
      this.getAllWishList();
    });
  }

  getAllWishList() {
    this.service.getWishListByUserId(this.currentPage).subscribe((res) => {
      if (res.content && Array.isArray(res.content)) {
        this.wishList = res.content.map((wishLists: any) => ({
          processedImg: 'data:image/jpeg;base64,' + wishLists.productImg,
          productName: wishLists.productName,
          price:wishLists.price,
          productId: wishLists.productId, 
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
    })
  }
  onPageChange(page: number) {
    this.currentPage = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {page: this.currentPage},
      queryParamsHandling: 'merge',
    });
    this.getAllWishList();
  }
}
