import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { SessionInformation } from '../interfaces/session-information';
import { SessionService } from './session.service';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private httpClient = inject(HttpClient);
  private pathService = environment.apiUrl + 'articles';
  private sessionService = inject(SessionService);
  private sessionInformation!: SessionInformation;

  constructor() {
    this.sessionInformation = this.sessionService
      .sessionInformation as SessionInformation;
  }

  public getAllOfUser(): Observable<Article[]> {
    return this.httpClient.get<Article[]>(
      `${this.pathService}/user/${this.sessionInformation.id}`,
    );
  }
}
