import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private baseUrl = 'http://localhost:8080/api/v1';
  constructor(private http:HttpClient) { }
  addToCart(productId:any):Observable<any>{
    const userId: number = Number(localStorage.getItem('userId'));
    const cartDto={
      productId:productId,
      userId,
    }
    return this.http.post(`${this.baseUrl}/cart/add` ,cartDto ,{headers:this.createHeader()})
  }
  private createHeader():HttpHeaders{
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + localStorage.getItem('accessToken')
    )
  }
  getCartByUserId():Observable<any>{
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.get(`${this.baseUrl}/cart/${userId}`  ,{headers:this.createHeader()})
  }
  deleteProductFromCart( productId: number): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.delete(`${this.baseUrl}/cart/delete/${userId}/${productId}`, { headers: this.createHeader() });
  }
  applyCoupon( code: string): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.post(`${this.baseUrl}/cart/${userId}/${code}`,null,{ headers: this.createHeader() });
  }



}
