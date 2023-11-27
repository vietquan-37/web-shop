import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private baseUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient,
  ) {
  }

  addCategory(categoryData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/category/create`, categoryData, {headers: this.createHeader()})
  }

  addCoupon(couponData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/coupon/create`, couponData, {headers: this.createHeader()})
  }

  private createHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + localStorage.getItem('accessToken')
    )
  }

  getAllCategory(): Observable<any> {
    return this.http.get(`${this.baseUrl}/category`, {headers: this.createHeader()})
  }
  changeUserStatus(request: any, userId: any): Observable<any> {
    return this.http.patch(`${this.baseUrl}/users/status/${userId}`, request, { headers: this.createHeader() });
  }

  getAllPlaceOrder(): Observable<any> {
    return this.http.get(`${this.baseUrl}/order`, {headers: this.createHeader()})
  }
  getAllUser(page:number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    return this.http.get(`${this.baseUrl}/users/allUsers`, {headers: this.createHeader(),params:params})
  }

  changeOrderStatus(orderId: any, orderStatus: any): Observable<any> {
    return this.http.get(`${this.baseUrl}/order/${orderId}/${orderStatus}`, {headers: this.createHeader()})
  }


  updateProduct(productId: any, productData: any): Observable<any> {
    return this.http.put(`${this.baseUrl}/product/update/${productId}`, productData, {headers: this.createHeader()})
  }

  addProduct(productData: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/product/create`, productData, {headers: this.createHeader()})
  }

  deleteProduct(id: number): Observable<any> {
    const headers = this.createHeader(); // Create headers
    return this.http.delete(`${this.baseUrl}/product/${id}`, {headers: headers});
  }


}
