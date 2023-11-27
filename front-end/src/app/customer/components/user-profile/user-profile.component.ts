import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {PublicService} from "../../../services/public.service";

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  info!: any;
  editedInfo: any; // New property to hold edited values
  isEditing = false;
  selectedFile: File | null = null;
  imagePreview: string | ArrayBuffer | null = null;
  isChangingPassword = false;
  changePasswordModel = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };
  hidePassword=true

  constructor(private service: CustomerService,
              private snackBar: MatSnackBar,
              private pService:PublicService) {
  }

  ngOnInit(): void {

    this.getUserInfo();
  }

  getUserInfo() {
    this.service.getUserInfo().subscribe((res) => {
      this.info = res;
      this.editedInfo = { ...res }; // Copy the user info for editing
    });
  }

  getImgUrl(img: number[] | null): string {
    if (img) {
      return 'data:image/jpeg;base64,' + img;
    } else {
      return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlobmpZJEKJBGI1h9WPvTdwmFXXnHezQFV4g&usqp=CAU";
    }
  }

  toggleEditMode() {
    this.isEditing = !this.isEditing;
    if(this.isEditing){
      this.isChangingPassword=false
    }
  }

  saveChanges() {
    const formData: FormData = new FormData();

    formData.append('firstname', this.editedInfo.firstname);
    formData.append('lastname', this.editedInfo.lastname);
    formData.append('phone', this.editedInfo.phone);

    if (this.editedInfo.img instanceof File) {
      formData.append('avatarFile', this.editedInfo.img);
    }

    this.service.changeUserInfo(formData).subscribe(
      (res) => {
        this.info = res;
        this.toggleEditMode();
      },
      (error) => {
        // Handle the error appropriately
        console.error('Error in saveChanges:', error);
      }
    );
  }



  OnFileSelected(events: any) {
    const file = events.target.files[0];
    if (file) {
      const allowedExtensions = /(\.png|\.jpg|\.jpeg)$/i;
      if (!allowedExtensions.test(file.name)) {
        this.snackBar.open('Please select a PNG or JPG file.', 'Close', { duration: 5000 });
        this.selectedFile = null;
        this.imagePreview = null;
      } else {
        this.selectedFile = file;
        this.previewImage();
        this.editedInfo.img = this.selectedFile; // Update editedInfo.img with the selected image
      }
    }

  }

  previewImage() {
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    if (this.selectedFile) {
      reader.readAsDataURL(this.selectedFile);
    }
  }
  deleteAvatar() {
    this.selectedFile = null;
    this.imagePreview ='data:image/jpeg;base64,' + this.info.img;
    this.editedInfo.img = null; // Remove the avatar from editedInfo
  }
  toggleChangePasswordMode() {
    this.isChangingPassword = !this.isChangingPassword;
    if(this.isChangingPassword){
      this.isEditing=false
    }
  }
  changePassword() {
    if (
      !this.changePasswordModel.currentPassword ||
      !this.changePasswordModel.newPassword ||
      this.changePasswordModel.newPassword !== this.changePasswordModel.confirmPassword
    ) {
      this.snackBar.open('Please fill in all fields and ensure the new passwords match.', 'Close', { duration: 5000 });
      return;
    }

    this.pService.changePassword(this.changePasswordModel).subscribe(
      () => {
        this.snackBar.open('Password changed successfully!', 'Close', { duration: 5000 });
        this.toggleChangePasswordMode();
      },
      (error) => {
        this.snackBar.open('Failed to change password. Please check your current password.', 'Close', { duration: 5000 });
        console.error(error);
      }
    );
  }
  togglePasswordVisibility(inputField: string): void {
    this.hidePassword = !this.hidePassword;

    // Optionally, you can add logic to update the input type based on the visibility state.
    const inputElement = document.getElementsByName(inputField)[0] as HTMLInputElement;
    inputElement.type = this.hidePassword ? 'password' : 'text';
  }
}

