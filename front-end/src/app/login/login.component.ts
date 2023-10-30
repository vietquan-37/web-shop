import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../services/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Router} from "@angular/router";
import {AuthenticationResponse} from "../models/authentication-response";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit{
  loginForm!: FormGroup;
  hidePassword = true;
  verifyForm!:FormGroup;
  mfaEnable=false;

  constructor(
    private builder: FormBuilder,
    private service: AuthenticationService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.builder.group({
      email: [null, Validators.required, Validators.email],
      password: [null, Validators.required]
    });
    this.verifyForm = this.builder.group({

      code: [null, [Validators.required]]
    });
  }

  togglePassword() {
    this.hidePassword = !this.hidePassword;
    const inputElement = document.querySelector('input[formControlName="password"]') as HTMLInputElement; // Cast the element to HTMLInputElement to avoid the 'possibly 'null'' error
    inputElement.type = this.hidePassword ? 'password' : 'text';
  }

  onSubmit(): void {

    this.service.authenticate(this.loginForm.value).subscribe(
      () => {
        if(localStorage.getItem('mfaEnable')=='false'){
        if (localStorage.getItem('role') == 'ADMIN') {

          this.snackBar.open('Login successfully', 'Close', {duration: 5000});

          this.router.navigate(['admin/dashboard'])
        }
        if (localStorage.getItem('role') == 'USER') {
          this.snackBar.open('Login successfully', 'Close', {duration: 5000});

          this.router.navigate(['customer/dashboard'])
        }

      }
        else{
          this.mfaEnable=true;
        }
      },
      (error) => {
        this.snackBar.open('Login Failed', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
      }
    )
  }
  verifyTfaCode() {
    const code = this.verifyForm.get('code')?.value;
    const email = this.loginForm.get('email')?.value;
    this.service.verifyCode({ code, email }).subscribe(
      (response: AuthenticationResponse) => {
        this.snackBar.open('Login successfully', 'Close', {duration: 5000});
        if (localStorage.getItem('role') == 'ADMIN') {

          this.snackBar.open('Login successfully', 'Close', {duration: 5000});

          this.router.navigate(['admin/dashboard'])
        }
        if (localStorage.getItem('role') == 'USER') {
          this.snackBar.open('Login successfully', 'Close', {duration: 5000});

          this.router.navigate(['customer/dashboard'])
        }
      },
      (error) => {
        this.snackBar.open('wrong code', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
      }


    )

  }


}
