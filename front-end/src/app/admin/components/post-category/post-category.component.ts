import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AdminService} from "../../service/admin.service";

@Component({
  selector: 'app-post-category',
  templateUrl: './post-category.component.html',
  styleUrls: ['./post-category.component.scss']
})

export class PostCategoryComponent implements OnInit {
  categoryForm!: FormGroup

  constructor(private builder: FormBuilder,
              private router: Router,
              private snackBar: MatSnackBar,
              private service: AdminService
  ) {
  }


  ngOnInit(): void {
    this.categoryForm = this.builder.group({
      name: [null, Validators.required],
      description: [null, Validators.required]
    });
  }


  addCategory() {
    if (this.categoryForm.invalid) {
      this.categoryForm.markAllAsTouched()
    } else {
      this.service.addCategory(this.categoryForm.value).subscribe((res) => {
        if (res.categoryId!=null) {
          this.snackBar.open('Create category successfully', 'Close', {duration: 5000})
          this.router.navigateByUrl('/admin/dashboard')
        } else {
          this.snackBar.open('Create category failed', 'Close', {duration: 5000})
          this.router.navigateByUrl('/admin/dashboard')
        }

      })

    }
  }
}
