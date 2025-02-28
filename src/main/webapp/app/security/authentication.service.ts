import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from 'environments/environment';
import { AuthenticationRequest, AuthenticationResponse } from 'app/security/authentication.model';


export const ADMIN = 'ADMIN';
export const DOCTOR = 'DOCTOR';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {

  http = inject(HttpClient);
  router = inject(Router);
  loginPath = environment.apiPath + '/authenticate';

  loginSuccessUrl: string = '/';

  getMessage(key: string, details?: any) {
    const messages: Record<string, string> = {
      loginRequired: $localize`:@@authentication.login.required:Please login to access this area.`,
      missingRole: $localize`:@@authentication.role.missing:You do not have sufficient rights to access this area.`,
      logoutSuccess: $localize`:@@authentication.logout.success:Your logout was successful.`
    };
    return messages[key];
  }

  checkAccessAllowed(route: ActivatedRouteSnapshot) {
    const roles = route.data['roles'];
    if (roles && !this.isLoggedIn()) {
      // show login page
      const targetUrl = this.router.getCurrentNavigation()?.finalUrl?.toString();
      if (targetUrl && targetUrl !== '/login') {
        this.loginSuccessUrl = targetUrl;
      }
      this.router.navigate(['/login'], {
            state: {
              msgInfo: this.getMessage('loginRequired')
            }
          });
      return false;
    } else if (roles && !this.hasAnyRole(roles)) {
      // show error page with message
      this.router.navigate(['/error'], {
            state: {
              errorStatus: '403',
              msgError: this.getMessage('missingRole')
            }
          });
      return false;
    }
    return true;
  }

  getLoginSuccessUrl() {
    return this.loginSuccessUrl;
  }

  login(authenticationRequest: AuthenticationRequest) {
    return this.http.post<AuthenticationResponse>(this.loginPath, authenticationRequest)
        .pipe(tap((data) => this.setSession(data)));
  }

  private setSession(authenticationResponse: AuthenticationResponse) {
    localStorage.setItem('access_token', authenticationResponse.accessToken!);
  }

  isLoggedIn() {
    // check token available
    if (!this.getToken()) {
      return false;
    }
    // check token not expired
    const tokenData = this.getTokenData();
    return Math.floor((new Date()).getTime() / 1000) < tokenData.exp;
  }

  hasAnyRole(requiredRoles: string[]) {
    const tokenData = this.getTokenData();
    return requiredRoles.some((requiredRole) => tokenData.roles.includes(requiredRole));
  }

  getToken() {
    return localStorage.getItem('access_token');
  }

  getTokenData() {
    const token = this.getToken()!!;
    return JSON.parse(atob(token.split('.')[1]));
  }

  getUserId() {
    return this.getTokenData().id
  }

  logout() {
    if (this.isLoggedIn()) {
      this.loginSuccessUrl = '/';
    }
    localStorage.removeItem('access_token');

    this.router.navigate(['/login'], {
          state: {
            msgInfo: this.getMessage('logoutSuccess')
          }
        });
  }

}
