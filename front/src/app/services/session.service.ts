import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { SessionInformation } from '../interfaces/session-information';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  public isLogged = false;
  public sessionInformation: SessionInformation | undefined;
  private isLoggedSubject = new BehaviorSubject<boolean>(this.isLogged);

  constructor() {
    const session: string | null = sessionStorage.getItem('sessionInformation');

    if (session) {
      this.sessionInformation = JSON.parse(session);

      if (!this.isTokenExpired(this.sessionInformation?.token as string)) {
        this.isLogged = true;
        this.next();
      }
    }
  }

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  public logIn(sessionInformation: SessionInformation): void {
    sessionStorage.setItem(
      'sessionInformation',
      JSON.stringify(sessionInformation),
    );
    this.sessionInformation = sessionInformation;
    this.isLogged = true;
    this.next();
  }

  public logOut(): void {
    sessionStorage.removeItem('sessionInformation');
    this.sessionInformation = undefined;
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }

  private isTokenExpired(token: string): boolean {
    const decodedToken: any = jwtDecode(token);
    const now = Math.floor(Date.now() / 1000);
    return decodedToken.exp < now;
  }
}
