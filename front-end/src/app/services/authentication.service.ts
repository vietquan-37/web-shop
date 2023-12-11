import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'; // Import HttpHeaders from @angular/common/http
import {Observable, tap, throwError} from "rxjs";
import {catchError} from 'rxjs/operators';
import {Router} from "@angular/router";
import {AuthenticationResponse} from "../models/authentication-response";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'https://ecommerce-vietquan.onrender.com/api/v1/auth';

  constructor(private http: HttpClient,
              private router: Router,
  ) {
  }

  register(inputData: any) {
    return this.http.post<AuthenticationResponse>(`${this.baseUrl}/register`, inputData);
  }
  reset(inputData: any) {
    return this.http.post(`${this.baseUrl}/forgot`, inputData);
  }

  verifyCode(inputData: any) {
    return this.http.post<AuthenticationResponse>(`${this.baseUrl}/verify`, inputData).pipe(
      tap((response: AuthenticationResponse) => {
        localStorage.setItem('accessToken', response.accessToken as string);
        localStorage.setItem('refreshToken', response.refreshToken as string);
        localStorage.setItem('role', response.role as string);
        localStorage.setItem('userId', response.userId?.toString() || '');

      })
    );
  }

  authenticate(inputData: any): Observable<AuthenticationResponse> {
    return this.http.post<AuthenticationResponse>(`${this.baseUrl}/authenticate`, inputData)
      .pipe(
        tap((response: AuthenticationResponse) => {
          localStorage.setItem('userId', response.userId?.toString() || '');
          localStorage.setItem('accessToken', response.accessToken as string);
          localStorage.setItem('refreshToken', response.refreshToken as string);
          localStorage.setItem('role', response.role as string);
          localStorage.setItem('mfaEnable', response.mfaEnable as string);


        })
      );
  }

  logout(): Observable<any> {

    const token = localStorage.getItem('refreshToken');

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.post(`${this.baseUrl}/logout`, {}, {headers})
      .pipe(
        catchError(error => {
          console.error('Logout error:', error);
          return throwError(error);
        }),
        tap(() => {
          console.log('Logout successful.');

          localStorage.clear();

        })
      );
  }
  resetPassword(request: any, token: string): Observable<any> {
    const resetUrl = `${this.baseUrl}/reset-password?token=${token}`; // Adjust the URL structure based on your backend API

    return this.http.post(resetUrl, request);
  }

  getNewAccessToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    return this.http.post(`${this.baseUrl}/refresh-token`, {
      token: refreshToken
    })
  }



  isAdminLogin(){
    if(localStorage.getItem('refreshToken')==null){
      return false
    }
    const role=localStorage.getItem('role')
    return role=='ADMIN';
  }
  isCustomerLogin(){
    if(localStorage.getItem('refreshToken')==null){
   return false
    }
  const role=localStorage.getItem('role')
    return role=='USER';
  }
}
