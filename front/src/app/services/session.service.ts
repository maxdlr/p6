import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { SessionInformation } from '../interfaces/session-information';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  public isLogged = false;
  public sessionInformation: SessionInformation | undefined;
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);
  private cookieService = inject(CookieService);

  constructor() {
    const cookie: string = this.cookieService.get('current-session');
    const cookieObject = cookie.length === 0 ? undefined : JSON.parse(cookie);
    if (cookieObject) {
      this.isLogged = true;
      this.sessionInformation = cookieObject;
    }
    this.next();
  }

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(sessionInformation: SessionInformation): void {
    this.cookieService.set(
      'current-session',
      JSON.stringify(sessionInformation),
    );

    this.sessionInformation = sessionInformation;
    this.isLogged = true;
    this.next();
  }

  public logOut(): void {
    this.cookieService.delete('current-session');
    this.sessionInformation = undefined;
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }
}
