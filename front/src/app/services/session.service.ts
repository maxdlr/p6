import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';
import { SessionInformation } from '../interfaces/session-information';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../pages/auth/services/auth.service';
import { TokenValidationRequest } from '../interfaces/token-validation-request';
import { SnackService } from './snack.service';
import { User } from '../interfaces/user';

// import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  public isLogged = false;
  public sessionInformation: SessionInformation | undefined;
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);
  private cookieService = inject(CookieService);
  private authService = inject(AuthService);
  private snackService = inject(SnackService);

  public $isLogged(): Observable<boolean> {
    this.validateToken();
    return this.isLoggedSubject.asObservable();
  }

  public logIn(sessionInformation: SessionInformation): void {
    // sessionStorage.setItem(
    //   'sessionInformation',
    //   JSON.stringify(sessionInformation),
    // );
    this.cookieService.set('token', sessionInformation.token);
    console.log('token on login', sessionInformation.token);

    this.sessionInformation = sessionInformation;
    this.isLogged = true;
    this.next();
  }

  public logOut(): void {
    // sessionStorage.removeItem('sessionInformation');
    this.cookieService.delete('token');
    this.sessionInformation = undefined;
    this.isLogged = false;
    this.next();
  }

  private validateToken(): Subscription {
    const storedToken: string | undefined = this.cookieService.get('token');

    console.log('storedToken', storedToken);

    return this.authService
      .me({ token: storedToken } as TokenValidationRequest)
      .subscribe({
        next: (user) => {
          this.sessionInformation = {
            username: user.username,
            email: user.email,
            token: storedToken,
            type: 'Bearer',
            id: user.id,
          };

          this.isLogged = true;
          this.next();
        },
        error: (error: any) => {
          this.sessionInformation = undefined;
          this.isLogged = false;
          console.error(error.error.message);
          this.snackService.error(error.error.message);
        },
      });
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }

  // private isTokenExpired(token: string): boolean {
  //   const decodedToken: any = jwtDecode(token);
  //   const now = Math.floor(Date.now() / 1000);
  //   return decodedToken.exp < now;
  // }
}
