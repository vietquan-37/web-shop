import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private baseUrl = 'https://ecommerce-vietquan.onrender.com/api/v1';

  constructor(private http: HttpClient) {
  }

  addToCart(productId: any, size: any): Observable<any> {
    const userId: number = Number(localStorage.getItem('userId'));
    const cartDto={
      productId:productId,
      userId,
      size:size
    }
    return this.http.post(`${this.baseUrl}/cart/add` ,cartDto ,{headers:this.createHeader()})
  }
  isProductInWishlist(productId:any):Observable<boolean>{
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.get<boolean>(`${this.baseUrl}/wishList/check/${userId}/${productId}`  ,{headers:this.createHeader()})
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
  getNonExpiredCoupon():Observable<any>{
    return this.http.get(`${this.baseUrl}/coupon/code`  ,{headers:this.createHeader()})
  }
  getWishListByUserId(page:number):Observable<any>{
    let params = new HttpParams();
    params = params.append('page', page.toString());
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.get(`${this.baseUrl}/wishList/${userId}`  ,{headers:this.createHeader(),params:params})
  }

  addOrDelete(productId:any):Observable<any>{
    const userId: number = Number(localStorage.getItem('userId'));

    return this.http.post(`${this.baseUrl}/wishList/${userId}/${productId}`,null
      ,{headers:this.createHeader()})
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

  userOrder(status: any[], page: number): Observable<any> {
    let params = new HttpParams();
    if (Array.isArray(status) && status.length > 0) {
      params = params.append('status', status.join(','));
    }

    params = params.append('page', page.toString());

    const userId: number = Number(localStorage.getItem('userId'));
    return this.http.get(`${this.baseUrl}/order/myOrder/${userId}`, {headers: this.createHeader(),params:params})
  }

  getProductById(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/product/get/${id}`, {headers: this.createHeader()})
  }

  getProductForReview(orderId: any): Observable<any> {
    return this.http.get(`${this.baseUrl}/review/${orderId}`, {headers: this.createHeader()})
  }

  getReviewByUsed(page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    const userId = localStorage.getItem('userId')
    return this.http.get(`${this.baseUrl}/review/user/${userId}`, {headers: this.createHeader(), params: params})
  }

  getUserInfo(): Observable<any> {
    const userId = localStorage.getItem('userId')
    return this.http.get(`${this.baseUrl}/users/${userId}`, {headers: this.createHeader()})
  }

}
