import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';
import { UserEditRequest } from '../pages/auth/interfaces/user-edit-request';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = environment.apiUrl + 'users';
  private httpClient = inject(HttpClient);

  public $getById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }

  public $edit(id: number, payload: UserEditRequest): Observable<User> {
    return this.httpClient.patch<User>(`${this.pathService}/${id}`, payload);
  }
}
