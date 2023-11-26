import { Component, OnInit } from '@angular/core';
import { CustomerService } from "../../service/customer.service";
import {MatSnackBar} from "@angular/material/snack-bar";

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
  constructor(private service: CustomerService,
              private snackBar:MatSnackBar) {}

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
  }

  saveChanges() {
    const formData: FormData = new FormData();
    formData.append('firstname', this.editedInfo.firstname);
    formData.append('lastname', this.editedInfo.lastname);
    formData.append('phone', this.editedInfo.phone);
    if(this.editedInfo.img!=null) {
      formData.append('avatarFile', this.editedInfo.img);
    }


    this.service.changeUserInfo(formData).subscribe((res) => {
      this.info = res;
      this.toggleEditMode();
    });
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
}

