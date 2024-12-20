import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';
import { User } from '../interfaces/user';
import { UserEditRequest } from '../pages/auth/interfaces/user-edit-request';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = environment.apiUrl + 'users';
  private httpClient = inject(HttpClient);

  public $getById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${id}`).pipe(
      map((user) => {
        user.createdAt = new Date(user.createdAt);
        user.updatedAt = new Date(user.updatedAt);
        return user;
      }),
    );
  }

  public $edit(id: number, payload: UserEditRequest): Observable<User> {
    return this.httpClient.put<User>(`${this.pathService}/${id}`, payload).pipe(
      map((user) => {
        user.createdAt = new Date(user.createdAt);
        user.updatedAt = new Date(user.updatedAt);
        return user;
      }),
    );
  }
}
