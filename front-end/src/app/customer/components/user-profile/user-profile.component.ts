import {Component, OnInit} from '@angular/core';
import {CustomerService} from "../../service/customer.service";

class byte {
}

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit{
  info!:any
  constructor(
    private service:CustomerService,

  ) {
  }
  ngOnInit(): void {
    this.getUserInfo();
  }
  getUserInfo(){
    this.service.getUserInfo().subscribe((res)=>{
      this.info=res;
    })
  }
  getImgUrl(img: number[] | null): string {
    if (img) {

      return 'data:image/jpeg;base64,'+img;
    } else {
      // Return the path to the default avatar if img is null
      return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQlobmpZJEKJBGI1h9WPvTdwmFXXnHezQFV4g&usqp=CAU";
    }
  }

}
