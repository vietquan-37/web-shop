import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {AuthenticationService} from "../services/authentication.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthenticationResponse} from "../models/authentication-response";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {
  hidePassword = true;
  registerForm!: FormGroup;
  verifyForm!: FormGroup;
  qrCode: string = "{}";
  showQrCode: boolean = false;


  constructor(private builder: FormBuilder,
              private snackBar: MatSnackBar,
              private router: Router,
              private service: AuthenticationService) {
  }

  ngOnInit(): void {
    this.registerForm = this.builder.group({
      firstname: [null, [Validators.required]],
      lastname: [null, [Validators.required]],
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]],
      confirmPassword: [null, [Validators.required]],
      mfaEnable:[false]

    });

    this.verifyForm = this.builder.group({

      code: [null, [Validators.required]]
    });
  }

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
  }


  onSubmit() {
    const password = this.registerForm.get('password')?.value;
    const confirmPassword = this.registerForm.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      this.snackBar.open('Passwords do not match', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
    } else {
      this.service.register(this.registerForm.value).subscribe(
        (response: AuthenticationResponse) => {

          if (this.registerForm.get('mfaEnable')?.value == true) {
            this.qrCode = response.secretImage!;
            this.showQrCode = true;

          }
          else{

            this.snackBar.open('Registration successful ', 'Close', {duration: 5000});
          // Navigate to login page or any other appropriate page after successful registration
          this.router.navigate(['login']);
          }

        },
        (error) => {
          this.snackBar.open('Registration failed', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
        }
      );
    }
  }

  verifyTfaCode() {
    const code = this.verifyForm.get('code')?.value;
    const email = this.registerForm.get('email')?.value;
    this.service.verifyCode({ code, email }).subscribe(
      (response: AuthenticationResponse) => {
        this.snackBar.open('Account created successfully', 'Close', {duration: 5000});
        this.router.navigate(['customer/dashboard']);
      },
      (error) => {
        if(error.status==405){
          this.snackBar.open('wrong code', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
        }
        this.snackBar.open('wrong code', 'Close', {duration: 5000, panelClass: 'error-snackbar'});
      }


    )

  }
}
