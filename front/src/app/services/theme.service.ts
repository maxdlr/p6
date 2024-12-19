import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Theme } from '../interfaces/theme';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private pathService = environment.apiUrl + 'themes';
  private httpClient = inject(HttpClient);

  public getAll(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(this.pathService);
  }
}
