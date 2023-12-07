import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MatSnackBar} from "@angular/material/snack-bar";
import {AuthenticationService} from "../services/authentication.service";

@Component({
  selector: 'app-reset',
  templateUrl: './reset.component.html',
  styleUrls: ['./reset.component.scss']
})
export class ResetComponent implements OnInit {
  resetForm!: FormGroup

  constructor(
    private builder: FormBuilder,
    private snackBar: MatSnackBar,
    private service: AuthenticationService
  ) {
  }

  ngOnInit() {
    this.resetForm = this.builder.group({
      email: [null, [Validators.email, Validators.required]]
    })

  }

  onSubmit() {
    if (this.resetForm.valid) {

      this.service.reset(this.resetForm.value).subscribe((res) => {
        this.snackBar.open('This link to reset password have been seen the your email ', 'Close', {duration: 5000})
      },
      (error)=>{
        if(error.status==404){
          this.snackBar.open('the email was not found', 'Close', {duration: 5000})
        }
      }
      )

    }
  }
}
