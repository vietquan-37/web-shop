import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from "rxjs";
import {inject} from "@angular/core";
import {MatSnackBar} from "@angular/material/snack-bar";

export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot):boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> => {
  const expectedRole = route.data['expectedRole'];
  const router=inject(MatSnackBar);
  const userRole = localStorage.getItem('role');
  if (userRole !== expectedRole) {
    router.open('dont have authorization to access this', 'Close', {duration: 5000, panelClass: 'error-snackbar'});

  return false;
}

return true;
};



