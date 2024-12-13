import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '../interfaces/login-request';
import { SessionInformation } from '../../../interfaces/session-information';
import { RegisterRequest } from '../interfaces/register-request';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private pathService = environment.apiUrl + 'auth';
  private httpClient = inject(HttpClient);

  public login(loginRequest: LoginRequest): Observable<SessionInformation> {
    return this.httpClient.post<SessionInformation>(
      `${this.pathService}/login`,
      loginRequest,
    );
  }

  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(
      `${this.pathService}/register`,
      registerRequest,
    );
  }
}
