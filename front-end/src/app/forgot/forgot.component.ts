import { Component } from '@angular/core';
import {AuthenticationService} from "../services/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-forgot',
  templateUrl: './forgot.component.html',
  styleUrls: ['./forgot.component.scss']
})
export class ForgotComponent {
  form!: FormGroup;
  token!: string;
  newPasswordVisible = false;
  confirmPasswordVisible = false;
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private builder: FormBuilder,
    private snackBar: MatSnackBar,
    private authService: AuthenticationService
  ) {
  }

  ngOnInit() {

      this.route.queryParams.subscribe(params => {
        this.token = params['token'];
      });



    this.form = this.builder.group({
      newPassword: [null, Validators.required],
      confirmPassword: [null, Validators.required]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const request = {
        newPassword: this.form.value.newPassword,
        confirmPassword: this.form.value.confirmPassword
      };

      this.authService.resetPassword(request, this.token).subscribe(
        (res) => {
          this.snackBar.open(res.message, 'Close', {duration: 5000});
          this.router.navigate(['/login']); // Redirect to login page after successful reset
        },
        (error) => {
          this.snackBar.open(error.error.message, 'Close', {duration: 5000});
        }
      );
    }
  }
  togglePasswordVisibility(field: string) {
    if (field === 'newPassword') {
      this.newPasswordVisible = !this.newPasswordVisible;
    } else if (field === 'confirmPassword') {
      this.confirmPasswordVisible = !this.confirmPasswordVisible;
    }
  }

}
