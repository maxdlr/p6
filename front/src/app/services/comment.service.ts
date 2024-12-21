import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommentRequest } from '../interfaces/comment-request';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private pathService = environment.apiUrl + 'comments';
  private httpClient = inject(HttpClient);

  public add(commentRequest: CommentRequest) {
    return this.httpClient.post<void>(this.pathService, commentRequest);
  }
}
