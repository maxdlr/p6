import { Component, inject, OnInit } from '@angular/core';
import { ArticleService } from '../../../../services/article.service';
import { Article } from '../../../../interfaces/model/article';
import { ActivatedRoute, Router } from '@angular/router';
import _ from 'lodash';
import { SessionService } from '../../../../services/session.service';
import { SessionInformation } from '../../../../interfaces/session-information';
import { CommentService } from '../../../../services/comment.service';

@Component({
  selector: 'app-article-read',
  standalone: false,
  templateUrl: './article-read.component.html',
  styleUrl: './article-read.component.scss',
})
export class ArticleReadComponent implements OnInit {
  public article!: Article;
  public isAuthor = false;
  public articleId!: string;
  public sessionInformation!: SessionInformation;
  protected readonly _ = _;
  protected readonly Number = Number;

  protected readonly confirm = confirm;
  private articleService = inject(ArticleService);
  private route = inject(ActivatedRoute);
  private sessionService = inject(SessionService);
  private commentService = inject(CommentService);
  private router = inject(Router);

  ngOnInit(): void {
    this.articleId = this.route.snapshot.paramMap.get('id') as string;
    this.refresh();
  }

  refresh() {
    this.articleService.getById(Number(this.articleId)).subscribe((article) => {
      this.article = article;
      this.sessionInformation = this.sessionService
        .sessionInformation as SessionInformation;
      this.isAuthor = this.sessionInformation.id === article.author.id;
    });
  }

  deleteComment(id: number) {
    this.commentService.confirmDelete(id, () => this.refresh());
  }

  delete(): void {
    this.articleService.confirmDelete(this.article, () =>
      this.router.navigate(['/articles']),
    );
  }
}
