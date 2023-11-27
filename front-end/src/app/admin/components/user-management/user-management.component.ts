// user-management.component.ts

import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../service/admin.service';
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {
  user: any[] = [];
  currentPage = 0;
  totalPages: number = 0;
  constructor(private service: AdminService,
              private snackBar:MatSnackBar) {}

  ngOnInit(): void {
    this.getAllUser();
  }

  getAllUser() {
    this.service.getAllUser(this.currentPage).subscribe((res) => {
      this.user = res.content;
      this.totalPages = res.totalPages;
    });
  }
  changeUserStatus(status:any,userId:any){
    this.service.changeUserStatus(status,userId).subscribe((res)=>{
      this.snackBar.open('Update successfully','Close',{duration:5000})
    },
      (error)=>{
        this.snackBar.open('Error while updating successfully','Close',{duration:5000})
    }
    )

  }

  onPageChange(page: number) {
    this.currentPage = page;
    this.getAllUser();
  }
}
