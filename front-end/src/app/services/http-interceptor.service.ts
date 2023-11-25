import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { switchMap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { AuthenticationService } from './authentication.service';
import { Router } from "@angular/router";

@Injectable()
export class Interceptor implements HttpInterceptor {

  private isRefreshing = false; // Flag to track if refresh token request is in progress

  constructor(private authService: AuthenticationService, private router: Router) { }

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    console.log('Request made to: ', req.url);

    return next.handle(req).pipe(
      catchError(error => {
        console.log('Error occurred:', error);

        if (error instanceof HttpErrorResponse) {
          if (error.status === 401) {
            // Handle 401 error (unauthorized access)
            if (!this.isRefreshing) {
              // If refresh token request is not in progress, try to refresh access token
              this.isRefreshing = true;
              console.log('Access token expired, refreshing');

              return this.authService.getNewAccessToken().pipe(
                switchMap((response: any) => {
                  // Update access token in request header
                  const clonedRequest = req.clone({
                    setHeaders: { Authorization: `Bearer ${response.accessToken}` }
                  });

                  // Reset refresh token flag and update local storage
                  this.isRefreshing = false;
                  localStorage.setItem('accessToken', response.accessToken);

                  // Retry the request with updated access token
                  return next.handle(clonedRequest);
                }),
                catchError(() => {
                  // If refresh token fails, log out the user and redirect to login
                  alert('Session has expired. Please log in again.');
                  localStorage.clear();
                  this.router.navigate(['login']);
                  return throwError('Access token is null');
                })
              );
            }
          } else if (error.status === 403) {
            // Handle 403 error (forbidden access)
            alert('this account have been log in another device');
            localStorage.clear();
            this.router.navigate(['login']);
            return throwError('Forbidden access');
          }
        }

        return throwError(error);
      })
    );
  }
}
