import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private baseUrl = 'http://localhost:8080/api/v1';
  constructor(private http:HttpClient) { }
  addToCart(productId:any,size:any):Observable<any>{
    const userId: number = Number(localStorage.getItem('userId'));
    const cartDto={
      productId:productId,
      userId,
      size:size
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
  deleteProductFromCart( productId: number,size:any): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.delete(`${this.baseUrl}/cart/delete/${userId}/${productId}/${size}`, { headers: this.createHeader() });
  }
  applyCoupon( code: string): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.post(`${this.baseUrl}/cart/${userId}/${code}`,null,{ headers: this.createHeader() });
  }
  changeUserInfo(userInfo: any): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));
    return this.http.put(`${this.baseUrl}/users/changeInfo/${userId}`,userInfo,{ headers: this.createHeader() });
  }


  increaseQuantity( productId: number,size:any): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));
    const cartDto={
      productId:productId,
      userId,
      size:size
    }
    return this.http.post(`${this.baseUrl}/cart/increase`,cartDto, { headers: this.createHeader() });
  }
  decreaseQuantity( productId: number,size:any): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));
    const cartDto={
      productId:productId,
      userId,
      size:size
    }
    return this.http.post(`${this.baseUrl}/cart/decrease`,cartDto, { headers: this.createHeader() });
  }
  placeOrder(orderDto:any): Observable<any> {
    return this.http.post(`${this.baseUrl}/order/placeOrder`,orderDto,{headers:this.createHeader()})
  }
  createReview(reviewDto:any,orderId:any): Observable<any> {
    return this.http.post(`${this.baseUrl}/review/create/${orderId}`,reviewDto,{headers:this.createHeader()})
  }
  userOrder(): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));
    return this.http.get(`${this.baseUrl}/order/myOrder/${userId}`,{headers:this.createHeader()})
  }
  getProductById(id:number): Observable<any>{
    return this.http.get(`${this.baseUrl}/product/get/${id}`,{headers:this.createHeader()})
  }
  getProductForReview(orderId:any): Observable<any>{
    return this.http.get(`${this.baseUrl}/review/${orderId}`,{headers:this.createHeader()})
  }
  getReviewByUsed(): Observable<any>{
  const  userId=localStorage.getItem('userId')
    return this.http.get(`${this.baseUrl}/review/user/${userId}`,{headers:this.createHeader()})
  }
  getUserInfo(): Observable<any>{
    const  userId=localStorage.getItem('userId')
    return this.http.get(`${this.baseUrl}/users/${userId}`,{headers:this.createHeader()})
  }

}
