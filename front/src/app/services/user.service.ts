import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { SessionInformation } from '../interfaces/session-information';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = environment.apiUrl + 'users';
  private httpClient = inject(HttpClient);
  private sessionInformation: SessionInformation | undefined;

  public getById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }
}
