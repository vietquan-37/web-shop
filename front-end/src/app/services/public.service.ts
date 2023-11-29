import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PublicService {
  private baseUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {
  }

  getAllProduct(page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', page.toString());


    return this.http.get(`${this.baseUrl}/product`, {
      headers: this.createHeader(),
      params: params
    });
  }

  getAllCoupon(): Observable<any> {
    return this.http.get(`${this.baseUrl}/coupon`, {headers: this.createHeader()})
  }

  getAllReviewByProduct(productId: any, page: number): Observable<any> {
    let params = new HttpParams();
    params = params.append('page', page.toString());
    return this.http.get(`${this.baseUrl}/review/get/${productId}`, {headers: this.createHeader(), params: params})
  }


  getAllProductByName(name: any, page: number) {
    let params = new HttpParams();
    if ( name.length > 0) {
      params = params.append('name', name);
    }
    params = params.append('page', page);
    return this.http.get(`${this.baseUrl}/product/search`, {headers: this.createHeader(),params:params});
  }

  changePassword(changePassDto:any):Observable<any> {
    return this.http.put(`${this.baseUrl}/users`, changePassDto,{headers: this.createHeader()});
  }


  private createHeader(): HttpHeaders {
    return new HttpHeaders().set(
      'Authorization', 'Bearer ' + localStorage.getItem('accessToken')
    )
  }
}
