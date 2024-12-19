import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FixturesService {
  private pathService = environment.apiUrl + 'fixtures';
  private httpClient = inject(HttpClient);

  public loadFixtures(): Observable<{ message: string }> {
    return this.httpClient.get<{ message: string }>(`${this.pathService}/load`);
  }

  public removeFixtures(): Observable<{ message: string }> {
    return this.httpClient.get<{ message: string }>(
      `${this.pathService}/prune`,
    );
  }
}
