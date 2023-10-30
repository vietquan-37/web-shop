import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { switchMap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import {Router} from "@angular/router";

@Injectable()
export class Interceptor implements HttpInterceptor {

  private isRefreshing = false; // Flag to track if refresh token request is in progress

  constructor(private authService: AuthenticationService,
  private router:Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    console.log('Request made to: ', req.url);

    return next.handle(req).pipe(
      catchError(error => {
        console.log('Error occurred:', error);

        if (error instanceof HttpErrorResponse && error.status === 401) {
          if (!this.isRefreshing) { // Check if refresh token request is not in progress
            this.isRefreshing = true; // Set the flag to indicate refresh token request in progress
            console.log('Access token expired, refreshing');

            return this.authService.getNewAccessToken().pipe(
              switchMap((response: any) => {

                const clonedRequest = req.clone({
                  setHeaders: { Authorization: `Bearer ${response.accessToken}` }
                });
                localStorage.setItem('accessToken', response.accessToken);
                this.isRefreshing = false; // Reset the flag after a successful refresh
                return next.handle(clonedRequest);
              })
            );
          } else {
            // If refresh token request is already in progress, log out the user
            alert("session has expired")
            localStorage.clear();
            this.router.navigate(['login']);

            return throwError('Access token is null');

          }
        }

        return throwError(error);
      })
    );
  }
}
