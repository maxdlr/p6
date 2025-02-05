import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Article } from '../interfaces/model/article';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ArticleRequest } from '../interfaces/payload/article-request';
import { DialogComponent } from '../components/dialog/dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { SnackService } from './snack.service';

@Injectable({
  providedIn: 'root',
})
export class ArticleService {
  readonly dialog = inject(MatDialog);
  private snackService = inject(SnackService);
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

  public confirmDelete(article: Article, onDeleted: () => void) {
    let commentNotice = '';

    if (article.comments.length > 0) {
      commentNotice = `Il sera supprimé avec ses ${article.comments.length} commentaires`;
    }
    const dialogRef = this.dialog.open(DialogComponent, {
      data: {
        title: `Suppression de ${article.title}`,
        content: `Êtes-vous sûr de vouloir supprimer cet article ? ${commentNotice}`,
        cancel: 'Annuler',
        confirm: 'Supprimer',
      },
    });

    dialogRef.componentInstance.confirmed.subscribe({
      next: () => {
        this.delete(article.id).subscribe(() => {
          this.snackService.inform('Article supprimé');
          onDeleted();
        });
      },
    });
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
