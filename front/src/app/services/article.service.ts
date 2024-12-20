import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Article } from '../interfaces/article';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private httpClient = inject(HttpClient);
  private pathService = environment.apiUrl + 'articles';

  public getAllOfUser(id: number): Observable<Article[]> {
    return this.httpClient
      .get<Article[]>(`${this.pathService}/user/${id}`)
      .pipe(
        map((articles: Article[]) => {
          return articles.map((article) => {
            article.createdAt = new Date(article.createdAt);
            article.updatedAt = new Date(article.updatedAt);
            return article;
          });
        }),
      );
  }
}
