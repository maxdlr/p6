import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequest } from '../interfaces/login-request';
import { SessionInformation } from '../../../interfaces/session-information';
import { RegisterRequest } from '../interfaces/register-request';
import { environment } from '../../../../environments/environment';
import { User } from '../../../interfaces/user';
import { TokenValidationRequest } from '../../../interfaces/token-validation-request';

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

  public me(tokenValidationRequest: TokenValidationRequest): Observable<User> {
    return this.httpClient.post<User>(
      `${this.pathService}/me`,
      tokenValidationRequest,
    );
  }
}
