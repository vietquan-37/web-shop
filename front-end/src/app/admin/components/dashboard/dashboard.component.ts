import {Component, OnInit} from '@angular/core';
import {AdminService} from '../../service/admin.service';
import {FormBuilder, FormGroup} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PublicService} from "../../../services/public.service";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {UpdateModalComponent} from "../update-modal/update-modal.component";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  products: any[] = [];
  searchForm!:FormGroup;
  currentPage: number = 0;
  totalPages: number = 0;
  pageNumbers: number[] = [];

  constructor(private service: AdminService,
              private pService: PublicService,
              private builder: FormBuilder,
              private snackBar: MatSnackBar,
              private dialog: MatDialog,
              private route: ActivatedRoute,
              private router: Router
  ) {
  }

  ngOnInit() {
    this.searchForm = this.builder.group({
      name: [null]
    });

    this.route.queryParams.subscribe(params => {
      this.currentPage = +params['page'] || 0;
      let nameFromQueryParam = params['name'];

      if (nameFromQueryParam !== '' && nameFromQueryParam !== undefined) {
        this.searchForm.patchValue({
          name: nameFromQueryParam
        });
        this.currentPage = 0;
        this.getAllProductsByName(nameFromQueryParam, this.currentPage);
      } else {
        // If nameFromQueryParam is an empty string or undefined, remove the 'name' parameter from the URL
        const queryParams = { page: this.currentPage };
        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: queryParams,
          queryParamsHandling: 'merge',
        });

        this.getAllProducts();
      }
    });
  }



  getAllProducts() {
    this.pService.getAllProduct(this.currentPage).subscribe((res: any) => {
      this.products = res.content.map((product: any) => ({
        processedImg: 'data:image/jpeg;base64,' + product.image,
        byteImages: product.byteImages.map((img: any) => 'data:image/jpeg;base64,' + img), // New line
        name: product.name,
        description: product.description,
        price: product.price,
        categoryName: product.categoryName,
        id: product.id,
        productSizes: product.productSizes
      }));
      this.totalPages = res.totalPages;

    });
  }
  submitForm(event: any) {
    if (this.searchForm.valid) {
      const name = this.searchForm.get('name')?.value;

      if (name != null && name.trim() !== '') {
        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: { name: name, page: this.currentPage },
          queryParamsHandling: 'merge',
        });
        this.currentPage=0
        this.getAllProductsByName(name, this.currentPage);
      } else {
        // Set page to 0 in the query parameters when the search form is empty
        const queryParams: { page: number; name?: string | null } = { page: 0 };

        // Check if 'name' parameter is present in the current query params
        if (this.route.snapshot.queryParams['name']) {
          queryParams.name = null; // Remove 'name' parameter from the query params
        }

        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: queryParams,
          queryParamsHandling: 'merge',
        });
        this.currentPage = 0;
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

    }, (error) => {
      this.snackBar.open('No records found.', 'Close', {duration: 5000});
    });
  }


  onPageChange(page: number) {

    this.currentPage = page;
    const name = this.searchForm.get('name')?.value;
    if (name) {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: {name: name, page: this.currentPage},
        queryParamsHandling: 'merge',
      });
      this.getAllProductsByName(name, this.currentPage);
    } else {
      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: {page: this.currentPage},
        queryParamsHandling: 'merge',
      });
      this.getAllProducts();
    }


  }


  deleteProduct(id: number) {
    this.service.deleteProduct(id).subscribe(
      () => {
        this.snackBar.open('Product deleted successfully', 'Close', {duration: 5000});
        this.getAllProducts(); // Refresh the product list after deletion
      },
      (error) => {
        this.snackBar.open('Product delete unsuccessful', 'Close', { duration: 5000 });
      }
    );
  }

  openUpdateDialog(product: any) {
    const dialogConfig:any = new MatDialogConfig();
    dialogConfig.data = { product ,imageData: product.processedImg,};

    const dialogRef = this.dialog.open(UpdateModalComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {

    });
  }
}
