import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  user: any[] = [];
  currentPage = 0;
  totalPages: number = 0;

  constructor(private service: AdminService, private snackBar: MatSnackBar,
              private route:ActivatedRoute,private router:Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.currentPage = +params['page'] || 0;
      if (this.currentPage < 0) {
        this.currentPage = 0
      }
      this.getAllUser()
    });
  }

  getAllUser() {
    this.service.getAllUser(this.currentPage).subscribe((res) => {
      this.user = res.content;
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
    });
  }

  changeUserStatus(status: any, userId: any) {
    const request = { request: status };

    this.service.changeUserStatus(request, userId).subscribe(
      (res) => {
        this.snackBar.open('Update successfully', 'Close', { duration: 5000 });
        setTimeout(() => {
          window.location.reload();
        }, 1000);

      },
      (error) => {
        this.snackBar.open('Error while updating successfully', 'Close', { duration: 5000 });
      }
    );
  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {  page: this.currentPage },
      queryParamsHandling: 'merge'
    });
    this.getAllUser();
  }
}
