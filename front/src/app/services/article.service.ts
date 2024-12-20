import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Article } from '../interfaces/article';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ArticleRequest } from '../interfaces/article-request';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  private httpClient = inject(HttpClient);
  private pathService = environment.apiUrl + 'articles';

  public getAllOfUser(id: number): Observable<Article[]> {
    return this.httpClient
      .get<Article[]>(`${this.pathService}/user/${id}`)
      .pipe(map((articles: Article[]) => this.transformArticles(articles)));
  }

  public getById(id: number): Observable<Article> {
    return this.httpClient
      .get<Article>(`${this.pathService}/${id}`)
      .pipe(map((article: Article) => this.transformArticle(article)));
  }

  public create(articleRequest: ArticleRequest) {
    return this.httpClient.post<Article>(`${this.pathService}`, articleRequest);
  }

  public update(id: number, articleRequest: ArticleRequest) {
    return this.httpClient.patch<Article>(
      `${this.pathService}/${id}`,
      articleRequest,
    );
  }

  public delete(id: number) {
    return this.httpClient.delete<void>(`${this.pathService}/${id}`);
  }

  private transformArticles(articles: Article[]): Article[] {
    return articles.map((article) => {
      return this.transformArticle(article);
    });
  }

  private transformArticle(article: Article): Article {
    article.createdAt = new Date(article.createdAt);
    article.updatedAt = new Date(article.updatedAt);
    article.comments.map((comment) => {
      comment.createdAt = new Date(comment.createdAt);
      comment.updatedAt = new Date(comment.updatedAt);
      return comment;
    });
    return article;
  }
}
