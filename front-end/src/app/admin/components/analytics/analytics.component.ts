import {Component, OnInit} from '@angular/core';
import {AdminService} from "../../service/admin.service";

@Component({
  selector: 'app-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss']
})
export class AnalyticsComponent implements OnInit{
  analytics:any
constructor(private service:AdminService
) {
}
ngOnInit() {
    this.getAnalytics()
}
getAnalytics(){
this.service.getAnalytic().subscribe((res)=>{
  this.analytics= res
})

}
}
