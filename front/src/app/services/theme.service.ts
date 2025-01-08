import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Theme } from '../interfaces/model/theme';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private pathService = environment.apiUrl + 'themes';
  private httpClient = inject(HttpClient);

  public getAll(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(this.pathService).pipe(
      map((themes: Theme[]) => {
        return themes.map((theme) => {
          theme.createdAt = new Date(theme.createdAt);
          theme.updatedAt = new Date(theme.updatedAt);
          return theme;
        });
      }),
    );
  }
}
