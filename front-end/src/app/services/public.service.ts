import { Injectable } from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PublicService {
  private baseUrl = 'http://localhost:8080/api/v1';

  constructor(private http:HttpClient) { }
  getAllProduct():Observable<any>{
    return this.http.get(`${this.baseUrl}/product`,{headers:this.createHeader()})
  }


  getAllProductByName(name:any):Observable<any>{
    return this.http.get(`${this.baseUrl}/product/search/${name}`,{headers:this.createHeader()})
  }
  private createHeader():HttpHeaders{
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + localStorage.getItem('accessToken')
    )
  }
}
